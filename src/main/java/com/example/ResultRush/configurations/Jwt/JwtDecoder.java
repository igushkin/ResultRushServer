package com.example.ResultRush.configurations.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ResultRush.configurations.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {

    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(SecretKey.MY_SECRET))
                .build()
                .verify(token);
    }
}