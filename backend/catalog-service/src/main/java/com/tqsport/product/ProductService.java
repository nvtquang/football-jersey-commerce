package com.tqsport.product;

import com.tqsport.product.repository.CategoryRepository;
import com.tqsport.product.repository.ProductImageRepository;
import com.tqsport.product.repository.ProductRepository;
import com.tqsport.product.repository.TeamRepository;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
public class ProductService {
    private final ProductRepository products;
    private final TeamRepository teams;
    private final CategoryRepository categories;
    private final ProductImageRepository images;

    public ProductService(ProductRepository products, TeamRepository teams, CategoryRepository categories,
                          ProductImageRepository images) {
        this.products = products;
        this.teams = teams;
        this.categories = categories;
        this.images = images;
    }

    @Transactional(readOnly = true)
    public List<ProductDtos.ProductSummary> search(String q, Long teamId, Long categoryId, int page, int size) {
        Specification<Product> spec = activeProducts();
        if (q != null && !q.isBlank()) {
            String term = "%" + q.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> {
                root.fetch("team", JoinType.LEFT);
                root.fetch("category", JoinType.LEFT);
                query.distinct(true);
                return cb.or(
                        cb.like(cb.lower(root.get("name")), term),
                        cb.like(cb.lower(root.join("team", JoinType.LEFT).get("name")), term)
                );
            });
        }
        if (teamId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("team").get("id"), teamId));
        }
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        }
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        return products.findAll(spec, PageRequest.of(safePage, safeSize)).stream().map(this::toSummary).toList();
    }

    @Transactional(readOnly = true)
    public ProductDtos.ProductSummary getBySlug(String slug) {
        Product product = products.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toSummary(product);
    }

    @Transactional
    public ProductDtos.ProductSummary create(ProductDtos.ProductRequest request) {
        Team team = teams.findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        Category category = categories.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Product product = new Product();
        product.name = request.name();
        product.slug = slugify(request.name());
        product.price = request.price();
        product.description = request.description();
        product.team = team;
        product.category = category;
        product.featured = request.featured();
        product.status = request.status() == null || request.status().isBlank() ? "ACTIVE" : request.status();
        products.save(product);
        upsertDefaultVariant(product, request.stockQuantity());
        upsertPrimaryImage(product, request.imageUrl());
        return toSummary(product);
    }

    @Transactional
    public ProductDtos.ProductSummary update(Long id, ProductDtos.ProductRequest request) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Team team = teams.findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        Category category = categories.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        product.name = request.name();
        product.slug = slugify(request.name());
        product.price = request.price();
        product.description = request.description();
        product.team = team;
        product.category = category;
        product.featured = request.featured();
        product.status = request.status() == null || request.status().isBlank() ? "ACTIVE" : request.status();
        upsertDefaultVariant(product, request.stockQuantity());
        upsertPrimaryImage(product, request.imageUrl());
        return toSummary(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.status = "ARCHIVED";
    }

    private Specification<Product> activeProducts() {
        return (root, query, cb) -> cb.equal(root.get("status"), "ACTIVE");
    }

    private ProductDtos.ProductSummary toSummary(Product product) {
        int stock = product.variants == null ? 0 : product.variants.stream().mapToInt(v -> v.stockQuantity).sum();
        String image = product.images == null ? null : product.images.stream().findFirst().map(i -> i.imageUrl).orElse(null);
        return new ProductDtos.ProductSummary(product.id, product.name, product.slug, product.price,
                product.team == null ? null : product.team.id,
                product.team == null ? "" : product.team.name,
                product.category == null ? null : product.category.id,
                product.category == null ? "" : product.category.name,
                image, stock, product.status, product.featured);
    }

    private void upsertDefaultVariant(Product product, int stockQuantity) {
        ProductVariant variant = product.variants.stream().findFirst().orElseGet(() -> {
            ProductVariant next = new ProductVariant();
            next.product = product;
            next.sku = slugify(product.name) + "-M";
            next.size = "M";
            next.color = "Default";
            product.variants.add(next);
            return next;
        });
        variant.stockQuantity = Math.max(stockQuantity, 0);
    }

    private void upsertPrimaryImage(Product product, String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        ProductImage image = images.findFirstByProductIdOrderBySortOrderAsc(product.id).orElseGet(() -> {
            ProductImage next = new ProductImage();
            next.product = product;
            next.sortOrder = 1;
            product.images.add(next);
            return next;
        });
        image.imageUrl = imageUrl;
        image.altText = product.name;
    }

    private String slugify(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
