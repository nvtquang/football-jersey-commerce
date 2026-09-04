package com.tqsport.gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}