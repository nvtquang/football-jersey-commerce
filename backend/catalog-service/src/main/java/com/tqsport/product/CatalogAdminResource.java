package com.tqsport.product;

import com.tqsport.product.repository.CategoryRepository;
import com.tqsport.product.repository.TeamRepository;
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
@RequestMapping(value = "/api/admin/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
public class CatalogAdminResource {
    public record TeamRequest(String name, String slug, String type, String country, String logoUrl) {}
    public record CategoryRequest(String name, String slug, Long parentId) {}

    private final TeamRepository teams;
    private final CategoryRepository categories;

    public CatalogAdminResource(TeamRepository teams, CategoryRepository categories) {
        this.teams = teams;
        this.categories = categories;
    }

    @GetMapping("/teams")
    public List<Team> teams() {
        return teams.findAll();
    }

    @PostMapping(value = "/teams", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Team createTeam(@RequestBody TeamRequest request) {
        Team team = new Team();
        applyTeam(team, request);
        return teams.save(team);
    }

    @PutMapping(value = "/teams/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Team updateTeam(@PathVariable Long id, @RequestBody TeamRequest request) {
        Team team = teams.findById(id).orElseThrow();
        applyTeam(team, request);
        return teams.save(team);
    }

    @DeleteMapping("/teams/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        teams.deleteById(id);
    }

    @GetMapping("/categories")
    public List<Category> categories() {
        return categories.findAll();
    }

    @PostMapping(value = "/categories", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category createCategory(@RequestBody CategoryRequest request) {
        Category category = new Category();
        applyCategory(category, request);
        return categories.save(category);
    }

    @PutMapping(value = "/categories/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        Category category = categories.findById(id).orElseThrow();
        applyCategory(category, request);
        return categories.save(category);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categories.deleteById(id);
    }

    private void applyTeam(Team team, TeamRequest request) {
        team.name = request.name();
        team.slug = request.slug();
        team.type = request.type();
        team.country = request.country();
        team.logoUrl = request.logoUrl();
    }

    private void applyCategory(Category category, CategoryRequest request) {
        category.name = request.name();
        category.slug = request.slug();
        category.parent = request.parentId() == null ? null : categories.findById(request.parentId()).orElse(null);
    }
}
