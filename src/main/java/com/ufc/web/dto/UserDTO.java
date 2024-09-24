package com.ufc.web.dto;

import com.ufc.web.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link User}
 */
public record UserDTO(String name, String email, String password, String icon, List<String> roles) implements Serializable {

    public User toUser() {
        User user = new User();
        user.name = this.name;
        user.email = this.email;
        user.setPassword(this.password);
        user.icon = this.icon;
        user.roles = this.roles;
        return user;
    }

}