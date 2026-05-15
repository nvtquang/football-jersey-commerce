package com.tqsport.admin;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;

@Path("/api/admin/uploads/images")
@RolesAllowed("ADMIN")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ImageUploadResource {
    public record ImageUploadRequest(@NotBlank String fileName, @NotBlank String contentBase64) {}
    public record ImageUploadResponse(String imageUrl, String altText) {}

    @POST
    public ImageUploadResponse upload(ImageUploadRequest request) {
        String safeName = Normalizer.normalize(request.fileName(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9.]+", "-");
        return new ImageUploadResponse("/uploads/" + Instant.now().toEpochMilli() + "-" + safeName, request.fileName());
    }
}

