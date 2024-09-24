package com.ufc.web.resource;

import com.ufc.web.dto.UserResponseDTO;
import com.ufc.web.entity.User;
import com.ufc.web.dto.UserRequestDTO;
import com.ufc.web.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/")
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/admin/users")
    @RolesAllowed("manager")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllUsers() {
        List<UserResponseDTO> userDTOList = userService.findAll().stream()
                .map(user -> new UserResponseDTO(user.id, user.name, user.email, null, user.icon, user.roles))
                .toList();
        return Response.status(Response.Status.OK).entity(userDTOList).build();
    }

    @GET
    @Path("/users")
    @RolesAllowed({"manager", "customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        User user = userService.getCurrentUser();
        UserResponseDTO currentUserDTO = new UserResponseDTO(user.id, user.name, user.email, null, user.icon, user.roles);
        return Response.status(Response.Status.OK).entity(currentUserDTO.toUser()).build();
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserRequestDTO userDTO) {
        User newUser = userService.createUser(userDTO.toUser());
        UserResponseDTO newUserDTO = new UserResponseDTO(newUser.id, newUser.name, newUser.email, null, newUser.icon, newUser.roles);
        return Response.status(Response.Status.CREATED).entity(newUserDTO.toUser()).build();
    }
}
