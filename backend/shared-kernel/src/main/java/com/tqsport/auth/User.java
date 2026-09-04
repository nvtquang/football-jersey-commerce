package com.tqsport.auth;

import com.tqsport.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    public String fullName;
    public String email;
    public String passwordHash;
    @Enumerated(EnumType.STRING)
    public UserRole role = UserRole.USER;
    public String phone;
    public boolean active = true;
}

