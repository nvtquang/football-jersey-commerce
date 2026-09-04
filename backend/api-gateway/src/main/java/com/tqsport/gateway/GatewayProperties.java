package com.tqsport.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tqsport.services")
public class GatewayProperties {
    private String auth = "http://localhost:8081";
    private String catalog = "http://localhost:8082";
    private String content = "http://localhost:8083";
    private String order = "http://localhost:8084";
    private String admin = "http://localhost:8085";
    private String cart = "http://localhost:8086";

    public String getAuth() { return auth; }
    public void setAuth(String auth) { this.auth = auth; }
    public String getCatalog() { return catalog; }
    public void setCatalog(String catalog) { this.catalog = catalog; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }
    public String getAdmin() { return admin; }
    public void setAdmin(String admin) { this.admin = admin; }
    public String getCart() { return cart; }
    public void setCart(String cart) { this.cart = cart; }
}