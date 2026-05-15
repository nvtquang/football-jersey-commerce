package com.tqsport.product;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CatalogResource {
    @GET
    @Path("/teams")
    public List<Team> teams() {
        return Team.<Team>listAll();
    }

    @GET
    @Path("/categories")
    public List<Category> categories() {
        return Category.<Category>listAll();
    }
}
