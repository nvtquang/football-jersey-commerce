package com.tqsport.product;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class ProductService {
    public List<ProductDtos.ProductSummary> search(String q, Long teamId, Long categoryId, int page, int size) {
        StringBuilder query = new StringBuilder("status = 'ACTIVE'");
        var params = new HashMap<String, Object>();
        if (q != null && !q.isBlank()) {
            query.append(" and (lower(name) like :q or lower(team.name) like :q)");
            params.put("q", "%" + q.toLowerCase(Locale.ROOT) + "%");
        }
        if (teamId != null) {
            query.append(" and team.id = :teamId");
            params.put("teamId", teamId);
        }
        if (categoryId != null) {
            query.append(" and category.id = :categoryId");
            params.put("categoryId", categoryId);
        }
        List<Product> products = Product.<Product>find(query.toString(), params).page(Page.of(page, size)).list();
        return products.stream().map(this::toSummary).toList();
    }

    public ProductDtos.ProductSummary getBySlug(String slug) {
        Product product = Product.find("slug", slug).firstResult();
        if (product == null) throw new NotFoundException("Product not found");
        return toSummary(product);
    }

    @Transactional
    public ProductDtos.ProductSummary create(ProductDtos.ProductRequest request) {
        Team team = Team.findById(request.teamId());
        Category category = Category.findById(request.categoryId());
        if (team == null || category == null) throw new NotFoundException("Team or category not found");
        Product product = new Product();
        product.name = request.name();
        product.slug = slugify(request.name());
        product.price = request.price();
        product.description = request.description();
        product.team = team;
        product.category = category;
        product.featured = request.featured();
        product.status = request.status() == null || request.status().isBlank() ? "ACTIVE" : request.status();
        product.persist();
        upsertDefaultVariant(product, request.stockQuantity());
        upsertPrimaryImage(product, request.imageUrl());
        return toSummary(product);
    }

    @Transactional
    public ProductDtos.ProductSummary update(Long id, ProductDtos.ProductRequest request) {
        Product product = Product.findById(id);
        if (product == null) throw new NotFoundException("Product not found");
        Team team = Team.findById(request.teamId());
        Category category = Category.findById(request.categoryId());
        if (team == null || category == null) throw new NotFoundException("Team or category not found");
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
        Product product = Product.findById(id);
        if (product == null) throw new NotFoundException("Product not found");
        product.status = "ARCHIVED";
    }

    private ProductDtos.ProductSummary toSummary(Product product) {
        int stock = product.variants.stream().mapToInt(v -> v.stockQuantity).sum();
        String image = product.images.stream().findFirst().map(i -> i.imageUrl).orElse(null);
        return new ProductDtos.ProductSummary(product.id, product.name, product.slug, product.price, product.team.id, product.team.name, product.category.id, product.category.name, image, stock, product.status, product.featured);
    }

    private void upsertDefaultVariant(Product product, int stockQuantity) {
        ProductVariant variant = product.variants.stream().findFirst().orElseGet(() -> {
            ProductVariant next = new ProductVariant();
            next.product = product;
            next.sku = slugify(product.name) + "-M";
            next.size = "M";
            next.color = "Default";
            next.persist();
            product.variants.add(next);
            return next;
        });
        variant.stockQuantity = Math.max(stockQuantity, 0);
    }

    private void upsertPrimaryImage(Product product, String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        ProductImage image = product.images.stream().findFirst().orElseGet(() -> {
            ProductImage next = new ProductImage();
            next.product = product;
            next.sortOrder = 1;
            next.persist();
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
