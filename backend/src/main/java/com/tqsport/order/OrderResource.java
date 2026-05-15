package com.tqsport.order;

import com.tqsport.auth.User;
import com.tqsport.order.OrderEntities.Order;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@RolesAllowed({"USER", "ADMIN"})
public class OrderResource {
    public record CheckoutRequest(String recipientName, String recipientPhone, String shippingAddress, String note) {}
    public record OrderSummary(Long id, String orderCode, String customer, String status, BigDecimal totalAmount) {}
    public record OrderStatusUpdate(String status) {}

    @GET
    public List<OrderSummary> history() {
        return Order.<Order>listAll().stream().map(this::toSummary).toList();
    }

    @POST
    @Transactional
    public OrderSummary checkout(CheckoutRequest request) {
        User user = User.findAll().firstResult();
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
        return toSummary(order);
    }

    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public List<OrderSummary> adminOrders() {
        return Order.<Order>listAll().stream().map(this::toSummary).toList();
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
}
