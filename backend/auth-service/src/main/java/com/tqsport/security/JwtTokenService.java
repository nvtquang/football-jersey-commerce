package com.tqsport.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqsport.auth.User;
import com.tqsport.auth.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtTokenService {
    private static final ObjectMapper JSON = new ObjectMapper();

    private final String secret;
    private final long ttlSeconds;

    public JwtTokenService(
            @Value("${tqsport.jwt.secret}") String secret,
            @Value("${tqsport.jwt.ttl-seconds:86400}") long ttlSeconds) {
        this.secret = secret;
        this.ttlSeconds = ttlSeconds;
    }

    public String issue(User user) {
        try {
            String header = base64Url(JSON.writeValueAsBytes(Map.of("alg", "HS256", "typ", "JWT")));
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", user.email);
            payload.put("name", user.fullName);
            payload.put("role", user.role == null ? UserRole.USER.name() : user.role.name());
            payload.put("iat", Instant.now().getEpochSecond());
            payload.put("exp", Instant.now().plusSeconds(ttlSeconds).getEpochSecond());
            String body = base64Url(JSON.writeValueAsBytes(payload));
            String signature = base64Url(hmacSha256(header + "." + body));
            return header + "." + body + "." + signature;
        } catch (Exception ex) {
            throw new IllegalStateException("Could not issue JWT", ex);
        }
    }

    private byte[] hmacSha256(String value) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
