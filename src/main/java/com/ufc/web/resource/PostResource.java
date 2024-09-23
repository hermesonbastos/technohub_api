package com.ufc.web.resource;

import com.ufc.web.dto.PostDTO;
import com.ufc.web.dto.UserDTO;
import com.ufc.web.entity.Post;
import com.ufc.web.entity.User;
import com.ufc.web.service.PostService;
import com.ufc.web.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/posts")
public class PostResource {

    private final PostService postService;
    private final UserService userService;

    @Inject
    public PostResource(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GET
    @RolesAllowed({"manager", "customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllPosts() {
        List<PostDTO> postDTOList = postService.findAll().stream()
                .map(post -> new PostDTO(post.title, post.description, post.link, post.category, post.author))
                .toList();
        return Response.status(Response.Status.FOUND).entity(postDTOList).build();
    }

    @GET
    @RolesAllowed({"manager","customer"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByUser(UserDTO userDTO) {
       var user = User.findByEmail(userDTO.email());
       if (user.isEmpty()) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       List<Post> posts = Post.findByAuthor(user.get());
       return Response.status(Response.Status.FOUND).entity(posts).build();
    }

    @POST
    @RolesAllowed({"manager", "customer"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(PostDTO postDTO) {
        Post newPost = postDTO.toPost();
        newPost.author = userService.getCurrentUser();
        postService.createPost(newPost);
        PostDTO newPostDTO = new PostDTO(newPost.title, newPost.description, newPost.link, newPost.category, newPost.author);
        return Response.status(Response.Status.CREATED).entity(newPostDTO).build();
    }


}
