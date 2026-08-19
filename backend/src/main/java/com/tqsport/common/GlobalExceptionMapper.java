package com.tqsport.common;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(404).entity(ApiError.of(404, exception.getMessage())).build();
        }
        if (exception instanceof NotAuthorizedException) {
            return Response.status(401).entity(ApiError.of(401, exception.getMessage())).build();
        }
        if (exception instanceof BadRequestException) {
            return Response.status(400).entity(ApiError.of(400, exception.getMessage())).build();
        }
        if (exception instanceof ConstraintViolationException validation) {
            var errors = validation.getConstraintViolations().stream()
                    .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage(), (a, b) -> a));
            return Response.status(400).entity(new ApiError(java.time.Instant.now(), 400, "Validation failed", errors)).build();
        }
        return Response.status(500).entity(ApiError.of(500, "Unexpected server error")).build();
    }
}

