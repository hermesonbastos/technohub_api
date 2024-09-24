package com.ufc.web.resource;

import com.ufc.web.dto.AuthDTO;
import com.ufc.web.dto.TokenDTO;
import com.ufc.web.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validate(TokenDTO tokenDTO) {
        boolean isValid = authService.validadate(tokenDTO);

        return isValid ?
                Response.status(Response.Status.OK).entity("Token válido").build() :
                Response.status(Response.Status.UNAUTHORIZED).entity("Token inválido").build();
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

            return Response.status(Response.Status.CREATED).entity(tokenResponse).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas").build();
        }
    }

}
