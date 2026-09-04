package com.tqsport.admin;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/admin/uploads/images", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageUploadResource {
    public record ImageUploadRequest(@NotBlank String fileName, @NotBlank String contentBase64) {}
    public record ImageUploadResponse(String imageUrl, String altText) {}

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ImageUploadResponse upload(@RequestBody ImageUploadRequest request) {
        String safeName = Normalizer.normalize(request.fileName(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9.]+", "-");
        return new ImageUploadResponse("/uploads/" + Instant.now().toEpochMilli() + "-" + safeName, request.fileName());
    }
}
