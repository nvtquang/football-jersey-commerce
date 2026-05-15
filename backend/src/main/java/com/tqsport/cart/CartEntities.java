package com.tqsport.cart;

import com.tqsport.auth.User;
import com.tqsport.common.BaseEntity;
import com.tqsport.product.ProductVariant;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

public class CartEntities {
    @Entity
    @Table(name = "carts")
    public static class Cart extends BaseEntity {
        @OneToOne public User user;
    }

    @Entity
    @Table(name = "cart_items")
    public static class CartItem extends BaseEntity {
        @ManyToOne public Cart cart;
        @ManyToOne public ProductVariant variant;
        public int quantity;
    }
}

