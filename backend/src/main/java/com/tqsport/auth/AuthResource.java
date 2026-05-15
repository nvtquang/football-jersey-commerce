package com.tqsport.auth;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AuthResource {
    @Inject AuthService authService;

    @POST
    @Path("/register")
    public AuthDtos.AuthResponse register(@Valid AuthDtos.RegisterRequest request) {
        return authService.register(request);
    }

    @POST
    @Path("/login")
    public AuthDtos.AuthResponse login(@Valid AuthDtos.LoginRequest request) {
        return authService.login(request);
    }
}

