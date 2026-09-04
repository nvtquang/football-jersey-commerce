package com.tqsport.order;

import com.tqsport.auth.User;
import com.tqsport.common.BaseEntity;
import com.tqsport.product.ProductVariant;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

public class OrderEntities {
    @Entity
    @Table(name = "orders")
    public static class Order extends BaseEntity {
        public String orderCode;
        @ManyToOne public User user;
        public String status;
        public BigDecimal totalAmount;
        public String recipientName;
        public String recipientPhone;
        public String shippingAddress;
        public String note;
    }

    @Entity
    @Table(name = "order_items")
    public static class OrderItem extends BaseEntity {
        @ManyToOne public Order order;
        @ManyToOne public ProductVariant variant;
        public String productName;
        public BigDecimal unitPrice;
        public int quantity;
    }
}

