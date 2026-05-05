package com.viratech.cadastrocliente.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String createToken(UserCredential user){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expires(30))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RoleBusinessException("Error to create toke JWT!");
        }
    }

    private Instant expires(Integer timeExpiresAt){
        return LocalDateTime.now().plusMinutes(timeExpiresAt).toInstant(ZoneOffset.of("-03:00"));
    }
}
