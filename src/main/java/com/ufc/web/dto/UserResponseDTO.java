package com.ufc.web.dto;

import com.ufc.web.entity.User;

import java.io.Serializable;
import java.util.List;

public record UserResponseDTO(long id, String name, String email, String password, String icon, List<String> roles) implements Serializable {

    public User toUser() {
        User user = new User();
        user.id = this.id;
        user.name = this.name;
        user.email = this.email;
        user.setPassword(this.password);
        user.icon = this.icon;
        user.roles = this.roles;
        return user;
    }
}