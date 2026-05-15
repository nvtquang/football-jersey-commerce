package com.tqsport.cart;

import com.tqsport.product.ProductVariant;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/cart")
@RolesAllowed({"USER", "ADMIN"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CartResource {
    public record CartLine(Long variantId, String productName, String size, int quantity) {}
    public record AddCartItem(Long variantId, @Min(1) int quantity) {}

    @GET
    public List<CartLine> currentCart() {
        return List.of();
    }

    @POST
    @Transactional
    public CartLine add(AddCartItem request) {
        ProductVariant variant = ProductVariant.findById(request.variantId());
        String name = variant == null || variant.product == null ? "Pending product lookup" : variant.product.name;
        String size = variant == null ? "M" : variant.size;
        return new CartLine(request.variantId(), name, size, request.quantity());
    }

    @PUT
    @Path("/{variantId}")
    public CartLine update(@PathParam("variantId") Long variantId, AddCartItem request) {
        ProductVariant variant = ProductVariant.findById(variantId);
        String name = variant == null || variant.product == null ? "Pending product lookup" : variant.product.name;
        String size = variant == null ? "M" : variant.size;
        return new CartLine(variantId, name, size, request.quantity());
    }

    @DELETE
    @Path("/{variantId}")
    public Response remove(@PathParam("variantId") Long variantId) {
        return Response.noContent().build();
    }
}
