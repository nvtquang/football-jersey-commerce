package com.tqsport.admin;

import com.tqsport.auth.repository.UserRepository;
import com.tqsport.order.repository.OrderRepository;
import com.tqsport.product.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/admin/stats", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminStatsResource {
    public record AdminStats(long products, long users, long orders, BigDecimal revenue) {}

    private final ProductRepository products;
    private final UserRepository users;
    private final OrderRepository orders;

    public AdminStatsResource(ProductRepository products, UserRepository users, OrderRepository orders) {
        this.products = products;
        this.users = users;
        this.orders = orders;
    }

    @GetMapping
    public AdminStats stats() {
        BigDecimal revenue = orders.findAll().stream()
                .map(order -> order.totalAmount == null ? BigDecimal.ZERO : order.totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new AdminStats(products.count(), users.count(), orders.count(), revenue);
    }
}
