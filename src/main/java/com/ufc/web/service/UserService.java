package com.ufc.web.service;

import com.ufc.web.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    private final JsonWebToken jwt;

    @Inject
    public UserService(JsonWebToken jwt) {
        this.jwt = jwt;
    }

    public List<User> findAll() {
        return User.findAll().list();
    }

    public User findByEmail(String username) {
        Optional<User> user = User.findByEmail(username);
        return user.orElse(null);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
        user.persist();
        return user;
    }

    public User getCurrentUser() {
        return findByEmail(jwt.getName());
    }

    public static boolean matchPassword(User user, String password) {
        return BcryptUtil.matches(password, user.getPassword());
    }

}
