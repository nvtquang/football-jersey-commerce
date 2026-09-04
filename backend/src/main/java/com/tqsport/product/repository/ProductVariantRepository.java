package com.tqsport.product.repository;

import com.tqsport.product.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findFirstByProductId(Long productId);
    Optional<ProductVariant> findFirstByProductIdAndSize(Long productId, String size);
}
