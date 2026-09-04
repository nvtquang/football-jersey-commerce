package com.tqsport.product;

import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {
    public String name;
    public String slug;
    public String type;
    public String country;
    public String logoUrl;
}

