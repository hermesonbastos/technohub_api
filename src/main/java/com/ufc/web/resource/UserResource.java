package com.ufc.web.resource;

import com.ufc.web.entity.User;
import com.ufc.web.dto.UserDTO;
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
        List<UserDTO> userDTOList = userService.findAll().stream()
                .map(user -> new UserDTO(user.email, null, user.icon, user.roles))
                .toList();
        return Response.ok(userDTOList).build();
    }

    @GET
    @Path("/users")
    @RolesAllowed({"manager", "customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        User user = userService.getCurrentUser();
        UserDTO currentUserDTO = new UserDTO(user.email, null, user.icon, user.roles);
        return Response.ok(currentUserDTO).build();
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO userDTO) {
        User newUser = userService.createUser(userDTO.toUser());
        UserDTO newUserDTO = new UserDTO(newUser.email, newUser.getPassword(), newUser.icon, newUser.roles);
        return Response.status(Response.Status.CREATED).entity(newUserDTO).build();
    }
}
