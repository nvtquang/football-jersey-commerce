package com.tqsport.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDtos {
    public record ProductSummary(Long id, String name, String slug, BigDecimal price, Long teamId, String team, Long categoryId, String category, String imageUrl, int stockQuantity, String status, boolean featured) {}
    public record ProductRequest(@NotBlank String name, @NotNull @DecimalMin("0.01") BigDecimal price, Long teamId, Long categoryId, String description, boolean featured, String status, String imageUrl, int stockQuantity) {}
}
