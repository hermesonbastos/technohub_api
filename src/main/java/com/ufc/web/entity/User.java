package com.ufc.web.entity;

import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "user_tb")
@JsonIgnoreProperties({"password", "roles", "posts"})
public class User extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    String password;

    @Column(name = "icon")
    public String icon;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Post> posts;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "role")
    public List<String> roles;


    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public static Optional<User> findByEmail(String email) {
        return User.find("email", email).firstResultOptional();
    }
}
