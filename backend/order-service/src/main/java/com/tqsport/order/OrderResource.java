package com.tqsport.order;

import com.tqsport.auth.User;
import com.tqsport.auth.repository.UserRepository;
import com.tqsport.order.OrderEntities.Order;
import com.tqsport.order.OrderEntities.OrderItem;
import com.tqsport.order.repository.OrderItemRepository;
import com.tqsport.order.repository.OrderRepository;
import com.tqsport.product.Product;
import com.tqsport.product.ProductVariant;
import com.tqsport.product.repository.ProductRepository;
import com.tqsport.product.repository.ProductVariantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {
    public record CheckoutItem(Long productId, String size, int quantity) {}
    public record CheckoutRequest(String recipientName, String recipientPhone, String shippingAddress, String note, List<CheckoutItem> items) {}
    public record OrderSummary(Long id, String orderCode, String customer, String status, BigDecimal totalAmount) {}
    public record OrderLine(Long id, String productName, String size, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {}
    public record OrderDetail(Long id, String orderCode, String customer, String status, BigDecimal totalAmount,
            String recipientName, String recipientPhone, String shippingAddress, String note, Instant createdAt,
            List<OrderLine> items) {}
    public record OrderStatusUpdate(String status) {}

    private final OrderRepository orders;
    private final OrderItemRepository orderItems;
    private final ProductRepository products;
    private final ProductVariantRepository variants;
    private final UserRepository users;

    public OrderResource(OrderRepository orders, OrderItemRepository orderItems, ProductRepository products,
                         ProductVariantRepository variants, UserRepository users) {
        this.orders = orders;
        this.orderItems = orderItems;
        this.products = products;
        this.variants = variants;
        this.users = users;
    }

    @GetMapping
    public List<OrderSummary> history() {
        return allOrders();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public OrderSummary checkout(@RequestBody CheckoutRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        User user = users.findAll().stream().findFirst().orElse(null);
        Order order = new Order();
        order.orderCode = "TS-" + System.currentTimeMillis();
        order.user = user;
        order.status = "PENDING";
        order.totalAmount = BigDecimal.ZERO;
        order.recipientName = request.recipientName();
        order.recipientPhone = request.recipientPhone();
        order.shippingAddress = request.shippingAddress();
        order.note = request.note();
        orders.save(order);

        for (CheckoutItem item : request.items()) {
            if (item.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item quantity");
            }

            Product product = products.findById(item.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not available"));
            if (!"ACTIVE".equals(product.status)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not available");
            }

            ProductVariant variant = findVariant(product, item.size());
            OrderItem orderItem = new OrderItem();
            orderItem.order = order;
            orderItem.variant = variant;
            orderItem.productName = product.name;
            orderItem.unitPrice = product.price;
            orderItem.quantity = item.quantity();
            orderItems.save(orderItem);

            order.totalAmount = order.totalAmount.add(product.price.multiply(BigDecimal.valueOf(item.quantity())));
        }

        return toSummary(orders.save(order));
    }

    @GetMapping("/admin")
    public List<OrderSummary> adminOrders() {
        return allOrders();
    }

    @GetMapping("/admin/{id}")
    public OrderDetail adminOrderDetail(@PathVariable Long id) {
        Order order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return toDetail(order);
    }

    @PatchMapping(value = "/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public OrderSummary updateStatus(@PathVariable Long id, @RequestBody OrderStatusUpdate request) {
        Order order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.status = request.status();
        return toSummary(orders.save(order));
    }

    private OrderSummary toSummary(Order order) {
        String customer = order.user == null ? order.recipientName : order.user.fullName;
        return new OrderSummary(order.id, order.orderCode, customer, order.status, order.totalAmount);
    }

    private OrderDetail toDetail(Order order) {
        String customer = order.user == null ? order.recipientName : order.user.fullName;
        List<OrderLine> items = orderItems.findByOrderId(order.id).stream()
                .map(item -> {
                    String size = item.variant == null ? "" : item.variant.size;
                    BigDecimal lineTotal = item.unitPrice.multiply(BigDecimal.valueOf(item.quantity));
                    return new OrderLine(item.id, item.productName, size, item.unitPrice, item.quantity, lineTotal);
                })
                .toList();
        return new OrderDetail(order.id, order.orderCode, customer, order.status, order.totalAmount,
                order.recipientName, order.recipientPhone, order.shippingAddress, order.note, order.createdAt, items);
    }

    private List<OrderSummary> allOrders() {
        return orders.findAll().stream().map(this::toSummary).toList();
    }

    private ProductVariant findVariant(Product product, String size) {
        if (size != null && !size.isBlank()) {
            return variants.findFirstByProductIdAndSize(product.id, size)
                    .orElseGet(() -> variants.findFirstByProductId(product.id).orElse(null));
        }
        return variants.findFirstByProductId(product.id).orElse(null);
    }
}
