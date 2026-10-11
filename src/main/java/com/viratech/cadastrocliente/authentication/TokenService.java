package com.viratech.cadastrocliente.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.exceptions.InvalidLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

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
                    .withClaim("type", "access")
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
                    .withClaim("type", "refresh")
                    .withExpiresAt(expires(60))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RoleBusinessException("Error to create refresh token!");
        }
    }

    private Instant expires(Integer timeExpiresAt){
        return Instant.now().plus(Duration.ofMinutes(timeExpiresAt));
    }

    public String verifyAccessToken(String token) {
        return verifyToken(token, "access");
    }

    public String verifyRefreshToken(String token) {
        return verifyToken(token, "refresh");
    }

    private String verifyToken(String token, String expectedType) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token);

            String tokenType = decodedJWT.getClaim("type").asString();

            if (!expectedType.equals(tokenType)) {
                throw new InvalidLoginException("Invalid token type.");
            }

            return decodedJWT.getSubject();

        } catch (JWTVerificationException exception) {
            throw new InvalidLoginException("Invalid or expired token.");
        }
    }
}
