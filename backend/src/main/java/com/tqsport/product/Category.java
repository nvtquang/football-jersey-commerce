package com.tqsport.product;

import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    public String name;
    public String slug;
    @ManyToOne
    public Category parent;
}

