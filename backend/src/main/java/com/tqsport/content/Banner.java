package com.tqsport.content;

import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity {
    public String title;
    public String subtitle;
    public String imageUrl;
    public String linkUrl;
    public String position;
    public boolean active = true;
    public int sortOrder;
}

