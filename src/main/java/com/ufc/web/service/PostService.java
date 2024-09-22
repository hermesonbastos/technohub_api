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
        post.persist();
    }

}
