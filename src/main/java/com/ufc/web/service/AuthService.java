package com.ufc.web.service;

import com.ufc.web.dto.AuthDTO;
import com.ufc.web.entity.User;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;

@ApplicationScoped
public class AuthService {

    private final String issuer;
    private final UserService userService;

    @Inject
    public AuthService(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer, UserService userService) {
        this.issuer = issuer;
        this.userService = userService;
    }

    public String authenticate(AuthDTO authDTO) {
        User user = userService.findByEmail(authDTO.username());
        if (user == null || !UserService.matchPassword(user, authDTO.password())) {
            throw new AuthenticationFailedException("Credenciais incorretas");
        }

        return Jwt.issuer(issuer)
                .upn(user.email)
                .groups(new HashSet<>(user.roles))
                .expiresIn(Duration.ofMinutes(25L))
                .sign();
    }
}
