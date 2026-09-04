package com.tqsport.auth;

import com.tqsport.auth.repository.UserRepository;
import com.tqsport.security.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {
    private final UserRepository users;
    private final JwtTokenService jwtTokenService;

    public AuthService(UserRepository users, JwtTokenService jwtTokenService) {
        this.users = users;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        String email = request.email().toLowerCase(Locale.ROOT).trim();
        if (users.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        User user = new User();
        user.fullName = request.fullName();
        user.email = email;
        user.passwordHash = "{bcrypt-placeholder}" + request.password();
        user.role = UserRole.USER;
        user.active = true;
        users.save(user);
        return issueToken(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        String email = request.email().toLowerCase(Locale.ROOT).trim();
        String password = request.password().trim();
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!user.active || user.passwordHash == null || !user.passwordHash.endsWith(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return issueToken(user);
    }

    private AuthDtos.AuthResponse issueToken(User user) {
        return new AuthDtos.AuthResponse(jwtTokenService.issue(user), user.email, user.fullName, user.role);
    }
}
