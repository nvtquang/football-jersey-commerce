package com.tqsport.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;

@ApplicationScoped
public class AuthService {
    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (User.count("email", request.email().toLowerCase()) > 0) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.fullName = request.fullName();
        user.email = request.email().toLowerCase();
        user.passwordHash = "{bcrypt-placeholder}" + request.password();
        user.role = UserRole.USER;
        user.persist();
        return issueToken(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = User.find("email", request.email().toLowerCase()).firstResult();
        if (user == null || !user.active || user.passwordHash == null || !user.passwordHash.endsWith(request.password())) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        return issueToken(user);
    }

    private AuthDtos.AuthResponse issueToken(User user) {
        return new AuthDtos.AuthResponse("", user.email, user.fullName, user.role);
    }
}

