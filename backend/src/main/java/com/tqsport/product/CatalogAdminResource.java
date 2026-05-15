package com.tqsport.product;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/admin/catalog")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CatalogAdminResource {
    public record TeamRequest(String name, String slug, String type, String country, String logoUrl) {}
    public record CategoryRequest(String name, String slug, Long parentId) {}

    @GET
    @Path("/teams")
    public List<Team> teams() {
        return Team.<Team>listAll();
    }

    @POST
    @Path("/teams")
    @Transactional
    public Team createTeam(TeamRequest request) {
        Team team = new Team();
        team.name = request.name();
        team.slug = request.slug();
        team.type = request.type();
        team.country = request.country();
        team.logoUrl = request.logoUrl();
        team.persist();
        return team;
    }

    @PUT
    @Path("/teams/{id}")
    @Transactional
    public Team updateTeam(@PathParam("id") Long id, TeamRequest request) {
        Team team = Team.findById(id);
        team.name = request.name();
        team.slug = request.slug();
        team.type = request.type();
        team.country = request.country();
        team.logoUrl = request.logoUrl();
        return team;
    }

    @DELETE
    @Path("/teams/{id}")
    @Transactional
    public Response deleteTeam(@PathParam("id") Long id) {
        Team.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/categories")
    public List<Category> categories() {
        return Category.<Category>listAll();
    }

    @POST
    @Path("/categories")
    @Transactional
    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.name = request.name();
        category.slug = request.slug();
        category.parent = request.parentId() == null ? null : Category.findById(request.parentId());
        category.persist();
        return category;
    }

    @PUT
    @Path("/categories/{id}")
    @Transactional
    public Category updateCategory(@PathParam("id") Long id, CategoryRequest request) {
        Category category = Category.findById(id);
        category.name = request.name();
        category.slug = request.slug();
        category.parent = request.parentId() == null ? null : Category.findById(request.parentId());
        return category;
    }

    @DELETE
    @Path("/categories/{id}")
    @Transactional
    public Response deleteCategory(@PathParam("id") Long id) {
        Category.deleteById(id);
        return Response.noContent().build();
    }
}
