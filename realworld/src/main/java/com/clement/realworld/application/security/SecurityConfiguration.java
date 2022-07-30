package com.clement.realworld.application.security;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration{

    private UserDetailsService userDetailsService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web
                            .ignoring().antMatchers("/users/open")
                            .antMatchers("/users/login")
                .antMatchers("/profiles")
                .and());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers(HttpMethod.GET, "/profiles/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user").authenticated()
                .antMatchers(HttpMethod.POST, "/profiles/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/profiles/**").authenticated()
                .antMatchers(HttpMethod.POST, "/articles/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/articles/**").authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic(withDefaults())
                .addFilterBefore(new JWTFilter(jwtProvider(), userDetailsService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTProvider jwtProvider() {
        return new JWTProvider();
    }


}
