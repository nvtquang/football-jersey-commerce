package com.tqsport.cart;

import com.tqsport.product.ProductVariant;
import com.tqsport.product.repository.ProductVariantRepository;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartResource {
    public record CartLine(Long variantId, String productName, String size, int quantity) {}
    public record AddCartItem(Long variantId, @Min(1) int quantity) {}

    private final ProductVariantRepository variants;

    public CartResource(ProductVariantRepository variants) {
        this.variants = variants;
    }

    @GetMapping
    public List<CartLine> currentCart() {
        return List.of();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CartLine add(@RequestBody AddCartItem request) {
        return toCartLine(request.variantId(), request.quantity());
    }

    @PutMapping(value = "/{variantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CartLine update(@PathVariable Long variantId, @RequestBody AddCartItem request) {
        return toCartLine(variantId, request.quantity());
    }

    @DeleteMapping("/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long variantId) {
    }

    private CartLine toCartLine(Long variantId, int quantity) {
        ProductVariant variant = variantId == null ? null : variants.findById(variantId).orElse(null);
        String name = variant == null || variant.product == null ? "Pending product lookup" : variant.product.name;
        String size = variant == null ? "M" : variant.size;
        return new CartLine(variantId, name, size, quantity);
    }
}
