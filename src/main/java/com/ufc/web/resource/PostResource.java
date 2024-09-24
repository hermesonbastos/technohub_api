package com.ufc.web.resource;

import com.ufc.web.dto.PostRequestDTO;
import com.ufc.web.dto.PostResponseDTO;
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
        List<PostResponseDTO> postDTOList = postService.findAll().stream()
                .map(post -> new PostResponseDTO(post.id, post.title, post.description, post.link, post.category, post.author))
                .toList();
        return Response.status(Response.Status.OK).entity(postDTOList).build();
    }

    @GET
    @Path("/user")
    @RolesAllowed({"manager","customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByUser(@QueryParam("email") String email) {
       var user = User.findByEmail(email);
       if (user.isEmpty()) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       List<Post> posts = Post.findByAuthor(user.get());
       return Response.status(Response.Status.OK).entity(posts).build();
    }

    @GET
    @Path("/category")
    @RolesAllowed({"manager", "customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostByCategory(@QueryParam("category") String category) {
        List<Post> posts = Post.findByCategory(category);
        return Response.status(Response.Status.OK).entity(posts).build();
    }

    @POST
    @RolesAllowed({"manager", "customer"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(PostRequestDTO postDTO) {
        Post newPost = postDTO.toPost();
        newPost.author = userService.getCurrentUser();
        postService.createPost(newPost);
        PostResponseDTO newPostDTO = new PostResponseDTO(newPost.id, newPost.title, newPost.description, newPost.link, newPost.category, newPost.author);
        return Response.status(Response.Status.CREATED).entity(newPostDTO.toPost()).build();
    }

    @PUT
    @RolesAllowed({"manager", "customer"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@QueryParam("id") long id, PostRequestDTO postDTO) {
        Post post = postDTO.toPost();
        post.id = id;
        Post updatedPost = postService.updatePost(post);

        return updatedPost != null ?
                Response.status(Response.Status.OK).entity(updatedPost).build() :
                Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @RolesAllowed({"manager", "customer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@QueryParam("id") Long id) {
        Post post = Post.findById(id);
        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        boolean deleted = postService.deletePost(post);
        return deleted ?
                Response.status(Response.Status.OK).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

}