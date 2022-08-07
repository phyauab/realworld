package com.clement.realworld.application.security;

import com.clement.realworld.application.exception.AppException;
import com.clement.realworld.infrastructure.user.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private JWTProvider jwtProvider;
    private UserDetailsService userDetailsService;
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        try {
            String jwt = jwtProvider.parseJWT(request);
            if(jwtProvider.validateJWT(jwt)) {
                String username = jwtProvider.getUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> customResponse = new HashMap<>();
            customResponse.put("status", "error");
            customResponse.put("message", "invalid authorization credentials");
            response.getWriter().write(objectMapper.writeValueAsString(customResponse));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

    }

}
