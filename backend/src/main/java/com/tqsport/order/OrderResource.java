package com.tqsport.order;

import com.tqsport.auth.User;
import com.tqsport.order.OrderEntities.Order;
import com.tqsport.order.OrderEntities.OrderItem;
import com.tqsport.product.Product;
import com.tqsport.product.ProductVariant;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@RolesAllowed({"USER", "ADMIN"})
public class OrderResource {
    @Inject JsonWebToken jwt;

    public record CheckoutItem(Long productId, String size, int quantity) {}
    public record CheckoutRequest(String recipientName, String recipientPhone, String shippingAddress, String note, List<CheckoutItem> items) {}
    public record OrderSummary(Long id, String orderCode, String customer, String status, BigDecimal totalAmount) {}
    public record OrderLine(Long id, String productName, String size, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {}
    public record OrderDetail(Long id, String orderCode, String customer, String status, BigDecimal totalAmount,
            String recipientName, String recipientPhone, String shippingAddress, String note, Instant createdAt,
            List<OrderLine> items) {}
    public record OrderStatusUpdate(String status) {}

    @GET
    public List<OrderSummary> history() {
        if (isAdmin()) {
            return allOrders();
        }
        User user = currentUser();
        return Order.<Order>find("user.id", user.id).stream().map(this::toSummary).toList();
    }

    @POST
    @Transactional
    public OrderSummary checkout(CheckoutRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        User user = currentUser();
        Order order = new Order();
        order.orderCode = "TS-" + System.currentTimeMillis();
        order.user = user;
        order.status = "PENDING";
        order.totalAmount = BigDecimal.ZERO;
        order.recipientName = request.recipientName();
        order.recipientPhone = request.recipientPhone();
        order.shippingAddress = request.shippingAddress();
        order.note = request.note();
        order.persist();

        for (CheckoutItem item : request.items()) {
            if (item.quantity() <= 0) {
                throw new BadRequestException("Invalid item quantity");
            }

            Product product = Product.findById(item.productId());
            if (product == null || !"ACTIVE".equals(product.status)) {
                throw new BadRequestException("Product is not available");
            }

            ProductVariant variant = findVariant(product, item.size());
            OrderItem orderItem = new OrderItem();
            orderItem.order = order;
            orderItem.variant = variant;
            orderItem.productName = product.name;
            orderItem.unitPrice = product.price;
            orderItem.quantity = item.quantity();
            orderItem.persist();

            order.totalAmount = order.totalAmount.add(product.price.multiply(BigDecimal.valueOf(item.quantity())));
        }

        return toSummary(order);
    }

    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public List<OrderSummary> adminOrders() {
        return allOrders();
    }

    @GET
    @Path("/admin/{id}")
    @RolesAllowed("ADMIN")
    public OrderDetail adminOrderDetail(@PathParam("id") Long id) {
        Order order = Order.findById(id);
        if (order == null) {
            throw new BadRequestException("Order not found");
        }
        return toDetail(order);
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("ADMIN")
    @Transactional
    public OrderSummary updateStatus(@PathParam("id") Long id, OrderStatusUpdate request) {
        Order order = Order.findById(id);
        order.status = request.status();
        return toSummary(order);
    }

    private OrderSummary toSummary(Order order) {
        String customer = order.user == null ? order.recipientName : order.user.fullName;
        return new OrderSummary(order.id, order.orderCode, customer, order.status, order.totalAmount);
    }

    private OrderDetail toDetail(Order order) {
        String customer = order.user == null ? order.recipientName : order.user.fullName;
        List<OrderLine> items = OrderItem.<OrderItem>find("order.id", order.id).stream()
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
        return Order.<Order>listAll().stream().map(this::toSummary).toList();
    }

    private boolean isAdmin() {
        return jwt != null && jwt.getGroups() != null && jwt.getGroups().contains("ADMIN");
    }

    private User currentUser() {
        if (jwt != null && jwt.getSubject() != null) {
            User user = User.find("email", jwt.getSubject()).firstResult();
            if (user != null) return user;
        }
        return User.findAll().firstResult();
    }

    private ProductVariant findVariant(Product product, String size) {
        if (size != null && !size.isBlank()) {
            ProductVariant sized = ProductVariant.find("product.id = ?1 and size = ?2", product.id, size).firstResult();
            if (sized != null) return sized;
        }
        return ProductVariant.find("product.id", product.id).firstResult();
    }
}
