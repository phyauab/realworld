package com.clement.realworld.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JWTProvider {

    private String jwtSecret = "realworld";

    private int accessTokenExpiryTimeMs = 600000;

    public String generateAccessToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + accessTokenExpiryTimeMs))
                .sign(Algorithm.HMAC256(jwtSecret));

    }

    // Retrieve jwt from header
    public String parseJWT(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.length() > 0 && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateJWT(String jwt) {
        try {
            JWT.decode(jwt);
            return true;
        } catch (JWTDecodeException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getUsername(String jwt) {
        return JWT.decode(jwt).getSubject();
    }

}
