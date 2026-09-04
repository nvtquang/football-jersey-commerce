package com.tqsport.product.repository;

import com.tqsport.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findFirstByProductIdOrderBySortOrderAsc(Long productId);
}
