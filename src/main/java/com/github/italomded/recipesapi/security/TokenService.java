package com.github.italomded.recipesapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.italomded.recipesapi.domain.ApplicationUser;
import org.springframework.stereotype.Service;


@Service
public class TokenService {
    public String generateToken(ApplicationUser user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("fsafsafa");
            String token = JWT.create()
                    .withIssuer("Recipes API")
                    .withSubject(user.getUsername())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException(exception);
        }
    }
}
