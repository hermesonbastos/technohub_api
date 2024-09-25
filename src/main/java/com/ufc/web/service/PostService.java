package com.ufc.web.service;

import com.ufc.web.entity.Post;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PostService {

    public List<Post> findAll() {
        return Post.findAll().list();
    }

    @Transactional
    public void createPost(Post post) {
        Post.persist(post);
    }

    @Transactional
    public Post updatePost(Post post) {
        Post existingPost = Post.findById(post.id);
        if (existingPost == null) {
            return null;
        }
        
        existingPost.title = post.title;
        existingPost.description = post.description;
        existingPost.link = post.link;
        existingPost.category = post.category;

        existingPost.persist();
        return existingPost;
    }

    @Transactional
    public boolean deletePost(Post post) {
        return Post.deletePostById(post.id);
    }

}
