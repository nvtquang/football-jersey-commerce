package com.tqsport.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;

    public GatewayAuthorizationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (requiresAdmin(path, method)) {
            JwtTokenService.JwtClaims claims = jwtTokenService.verify(resolveBearerToken(request));
            if (claims == null) {
                writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is missing or invalid");
                return;
            }
            if (!"ADMIN".equals(claims.role())) {
                writeError(response, HttpServletResponse.SC_FORBIDDEN, "ADMIN role is required");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAdmin(String path, String method) {
        if (HttpMethod.OPTIONS.matches(method)) return false;
        if (path.startsWith("/actuator") || path.startsWith("/api/auth")) return false;
        if (path.startsWith("/api/admin/")) return true;
        if (path.startsWith("/api/orders/admin")) return true;
        if (path.startsWith("/api/orders/") && HttpMethod.PATCH.matches(method)) return true;
        if (path.startsWith("/api/products") && !HttpMethod.GET.matches(method)) return true;
        if (path.startsWith("/api/banners") && !HttpMethod.GET.matches(method)) return true;
        return false;
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) return null;
        return authorization.substring("Bearer ".length()).trim();
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
