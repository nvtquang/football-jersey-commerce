package com.tqsport.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {
    @ManyToOne @JsonIgnore public Product product;
    public String imageUrl;
    public String altText;
    public int sortOrder;
}
