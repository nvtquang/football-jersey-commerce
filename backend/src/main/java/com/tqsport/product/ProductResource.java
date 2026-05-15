package com.tqsport.product;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProductResource {
    @Inject ProductService service;

    @GET
    public List<ProductDtos.ProductSummary> search(@QueryParam("q") String q, @QueryParam("teamId") Long teamId,
            @QueryParam("categoryId") Long categoryId, @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("12") int size) {
        return service.search(q, teamId, categoryId, page, size);
    }

    @GET
    @Path("/{slug}")
    public ProductDtos.ProductSummary detail(@PathParam("slug") String slug) {
        return service.getBySlug(slug);
    }

    @POST
    @RolesAllowed("ADMIN")
    public ProductDtos.ProductSummary create(@Valid ProductDtos.ProductRequest request) {
        return service.create(request);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public ProductDtos.ProductSummary update(@PathParam("id") Long id, @Valid ProductDtos.ProductRequest request) {
        return service.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
