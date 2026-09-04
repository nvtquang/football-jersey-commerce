package com.tqsport.product;

import com.tqsport.product.repository.CategoryRepository;
import com.tqsport.product.repository.TeamRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
public class CatalogResource {
    private final TeamRepository teams;
    private final CategoryRepository categories;

    public CatalogResource(TeamRepository teams, CategoryRepository categories) {
        this.teams = teams;
        this.categories = categories;
    }

    @GetMapping("/teams")
    public List<Team> teams() {
        return teams.findAll();
    }

    @GetMapping("/categories")
    public List<Category> categories() {
        return categories.findAll();
    }
}
