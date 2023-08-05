package com.example.ResultRush.configurations.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ResultRush.configurations.SecretKey;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    public String issue(Request request) {
        var now = Instant.now();

        return JWT.create()
                .withSubject(String.valueOf(request.userId))
.withClaim("e", request.getEmail())
                .withClaim("au", request.getRoles())
                .sign(Algorithm.HMAC256(SecretKey.MY_SECRET));
    }

    @Getter
    @Builder
    public static class Request {
        private final Integer userId;
        private final String email;
        private final List<String> roles;
    }
}