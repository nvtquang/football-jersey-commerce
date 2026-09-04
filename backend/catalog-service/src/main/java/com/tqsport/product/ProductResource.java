package com.tqsport.product;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {
    private final ProductService service;

    public ProductResource(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductDtos.ProductSummary> search(@RequestParam(required = false) String q,
                                                   @RequestParam(required = false) Long teamId,
                                                   @RequestParam(required = false) Long categoryId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "12") int size) {
        return service.search(q, teamId, categoryId, page, size);
    }

    @GetMapping("/{slug}")
    public ProductDtos.ProductSummary detail(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductDtos.ProductSummary create(@Valid @RequestBody ProductDtos.ProductRequest request) {
        return service.create(request);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductDtos.ProductSummary update(@PathVariable Long id, @Valid @RequestBody ProductDtos.ProductRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
