package com.viratech.cadastrocliente.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final Algorithm algorithm;

    public TokenService(
            @Value("${api.security.token.secret}")
                    String secret)
    {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String createToken(UserCredential user){
        try {
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withClaim("name", user.getUser().getName())
                    .withExpiresAt(expires(30))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RoleBusinessException("Error to create token!");
        }
    }

    public String createRefreshToken(UserCredential user){
        try {
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(expires(60))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RoleBusinessException("Error to create refresh token!");
        }
    }

    private Instant expires(Integer timeExpiresAt){
        return Instant.now().plus(Duration.ofMinutes(timeExpiresAt));
    }

    public String verifyToken(String token){

        DecodedJWT decodedJWT;
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();

        } catch (JWTVerificationException exception){
            throw new RoleBusinessException("Error to validate token!");
        }
    }
}
