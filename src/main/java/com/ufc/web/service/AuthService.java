package com.ufc.web.service;

import com.ufc.web.dto.AuthDTO;
import com.ufc.web.dto.TokenDTO;
import com.ufc.web.entity.User;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

@ApplicationScoped
public class AuthService {

    private final String issuer;
    private final JWTParser parser;
    private final UserService userService;

    @Inject
    public AuthService(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer, JWTParser parser, UserService userService) {
        this.issuer = issuer;
        this.parser = parser;
        this.userService = userService;
    }

    public boolean validate(TokenDTO tokenDTO) {
        try {
            JsonWebToken jwt = parser.parse(tokenDTO.token());

            if (!jwt.getIssuer().equals(issuer)) {
                return false;
            }

            Instant expirationTime = Instant.ofEpochSecond(jwt.getExpirationTime());

            return expirationTime == null || !Instant.now().isAfter(expirationTime);
        } catch (ParseException e) {
            return false;
        }
    }

    public String authenticate(AuthDTO authDTO) {
        User user = userService.findByEmail(authDTO.email());
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
