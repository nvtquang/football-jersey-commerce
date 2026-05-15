package com.tqsport.product;

import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    public String name;
    public String slug;
    public String description;
    public BigDecimal price;
    @ManyToOne public Team team;
    @ManyToOne public Category category;
    public String status = "ACTIVE";
    public boolean featured;
    @OneToMany(mappedBy = "product") public List<ProductVariant> variants = new ArrayList<>();
    @OneToMany(mappedBy = "product") public List<ProductImage> images = new ArrayList<>();
}

