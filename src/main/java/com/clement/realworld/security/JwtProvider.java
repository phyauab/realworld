package com.clement.realworld.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtProvider {

    private String jwtSecret = "realworld";

    private int accessTokenExpiryTimeMs = 600000;

    public String generateAccessToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + accessTokenExpiryTimeMs))
                .sign(Algorithm.HMAC256(jwtSecret));

    }

    // Retrieve jwt from header
    public String parseJWT(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.length() > 0 && bearerToken.startsWith("Token ")) {
            return bearerToken.substring("Token ".length());
        }
        return null;
    }

    public boolean validateJWT(String jwt) {
        JWT.decode(jwt);
        return true;
    }

    public String getUsername(String jwt) {
        return JWT.decode(jwt).getSubject();
    }

}
