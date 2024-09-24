package com.ufc.web.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "post_tb")
public class Post extends PanacheEntity {

    @Column(nullable = false, length = 100)
    public String title;

    @Column(nullable = false, length = 500)
    public String description;

    @Column(nullable = false, length = 300)
    public String link;

    @Column(nullable = false, length = 50)
    public String category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    public User author;

    public static Post findById(long id) {
        return find("id", id).firstResult();
    }

    public static List<Post> findByAuthor(User author) {
        return list("author", author);
    }

    public static boolean deletePostById(long id) {
        return deleteById(id);
    }

    public static Post updatePost(Post tempPost) {
        Post post = (Post) findByIdOptional(tempPost.id).orElse(null);
        if (post != null) {
            post.title = tempPost.title;
            post.description = tempPost.description;
            post.link = tempPost.link;
            post.category = tempPost.category;
            post.persist();
        }
        return post;
    }

    public static List<Post> findByCategory(String category) {
        return list("category", category);
    }
}