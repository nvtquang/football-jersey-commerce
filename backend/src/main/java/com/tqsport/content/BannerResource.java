package com.tqsport.content;

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

@Path("/api/banners")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class BannerResource {
    @GET
    public List<Banner> active() {
        return Banner.list("order by sortOrder");
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    public Banner create(Banner banner) {
        banner.persist();
        return banner;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    public Banner update(@PathParam("id") Long id, Banner request) {
        Banner banner = Banner.findById(id);
        banner.title = request.title;
        banner.subtitle = request.subtitle;
        banner.imageUrl = request.imageUrl;
        banner.linkUrl = request.linkUrl;
        banner.position = request.position;
        banner.active = request.active;
        banner.sortOrder = request.sortOrder;
        return banner;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        Banner.deleteById(id);
        return Response.noContent().build();
    }
}
