package com.tqsport.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {
    @ManyToOne @JsonIgnore public Product product;
    public String sku;
    public String size;
    public String color;
    public int stockQuantity;
}
