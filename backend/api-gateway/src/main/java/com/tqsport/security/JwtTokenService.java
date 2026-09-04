package com.tqsport.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtTokenService {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final String secret;

    public JwtTokenService(@Value("${tqsport.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public JwtClaims verify(String token) {
        try {
            String[] parts = token == null ? new String[0] : token.split("\\.");
            if (parts.length != 3) return null;
            String expected = base64Url(hmacSha256(parts[0] + "." + parts[1]));
            if (!constantTimeEquals(expected, parts[2])) return null;
            Map<String, Object> payload = JSON.readValue(Base64.getUrlDecoder().decode(parts[1]), MAP_TYPE);
            long exp = ((Number) payload.getOrDefault("exp", 0)).longValue();
            if (exp < Instant.now().getEpochSecond()) return null;
            return new JwtClaims(
                    String.valueOf(payload.getOrDefault("sub", "")),
                    String.valueOf(payload.getOrDefault("role", "USER")));
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] hmacSha256(String value) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean constantTimeEquals(String left, String right) {
        if (left == null || right == null || left.length() != right.length()) return false;
        int result = 0;
        for (int index = 0; index < left.length(); index++) {
            result |= left.charAt(index) ^ right.charAt(index);
        }
        return result == 0;
    }

    private static String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    public record JwtClaims(String email, String role) {}
}
