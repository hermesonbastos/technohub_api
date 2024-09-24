package com.ufc.web.resource;

import com.ufc.web.dto.AuthDTO;
import com.ufc.web.dto.TokenDTO;
import com.ufc.web.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthResource {

    private final AuthService authService;

    @Inject
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @PermitAll
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthDTO request) {
        try {
            String token = authService.authenticate(request);
            TokenDTO tokenResponse = new TokenDTO(token);

            return Response.status(Response.Status.CREATED)
                    .entity(tokenResponse).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciais inválidas").build();
        }
    }

}
