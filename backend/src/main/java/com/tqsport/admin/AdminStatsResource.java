package com.tqsport.admin;

import com.tqsport.auth.User;
import com.tqsport.order.OrderEntities.Order;
import com.tqsport.product.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/api/admin/stats")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AdminStatsResource {
    public record AdminStats(long products, long users, long orders, BigDecimal revenue) {}

    @GET
    public AdminStats stats() {
        BigDecimal revenue = Order.<Order>listAll().stream()
                .map(order -> order.totalAmount == null ? BigDecimal.ZERO : order.totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new AdminStats(Product.count(), User.count(), Order.count(), revenue);
    }
}
