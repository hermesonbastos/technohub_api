package com.ufc.web.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

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

}