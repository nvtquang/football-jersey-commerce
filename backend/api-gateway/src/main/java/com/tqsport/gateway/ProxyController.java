package com.tqsport.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

@RestController
public class ProxyController {
    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of("host", "connection", "content-length", "transfer-encoding");
    private final RestTemplate restTemplate;
    private final GatewayProperties services;

    public ProxyController(RestTemplate restTemplate, GatewayProperties services) {
        this.restTemplate = restTemplate;
        this.services = services;
    }

    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        String path = request.getRequestURI();
        String target = resolveTarget(path);
        URI uri = UriComponentsBuilder.fromHttpUrl(target)
                .path(path)
                .query(request.getQueryString())
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(name -> {
            if (!HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                headers.put(name, Collections.list(request.getHeaders(name)));
            }
        });

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        return restTemplate.exchange(uri, method, new HttpEntity<>(body, headers), byte[].class);
    }

    private String resolveTarget(String path) {
        if (path.startsWith("/api/auth")) return services.getAuth();
        if (path.startsWith("/api/products") || path.startsWith("/api/catalog") || path.startsWith("/api/admin/catalog")) return services.getCatalog();
        if (path.startsWith("/api/orders")) return services.getOrder();
        if (path.startsWith("/api/banners") || path.startsWith("/api/admin/uploads")) return services.getContent();
        if (path.startsWith("/api/admin/stats") || path.startsWith("/api/admin/users")) return services.getAdmin();
        if (path.startsWith("/api/cart")) return services.getCart();
        throw new IllegalArgumentException("No service route configured for " + path);
    }
}