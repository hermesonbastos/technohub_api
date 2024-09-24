package com.ufc.web.dto;

import com.ufc.web.entity.Post;
import com.ufc.web.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link Post}
 */
public record PostResponseDTO(long id, String title, String description, String link, String category, User author) implements Serializable {

    public Post toPost() {
        Post post = new Post();
        post.id = this.id;
        post.title = this.title;
        post.description = this.description;
        post.link = this.link;
        post.category = this.category;
        return post;
    }

}