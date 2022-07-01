package com.clement.realworld.infrastructure.user;

import com.clement.realworld.application.security.JWTProvider;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.UserService;
import com.clement.realworld.domain.user.dto.UserDto;
import com.clement.realworld.domain.user.dto.UserLoginDto;
import com.clement.realworld.domain.user.dto.UserRegistrationDto;
import com.clement.realworld.domain.user.role.ERole;
import com.clement.realworld.domain.user.role.Role;
import com.clement.realworld.domain.user.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTProvider jwtProvider;
    private AuthenticationManager authenticationManager;

    @Override
    public UserDto register(UserRegistrationDto userRegistrationDto) {

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                        .orElseThrow(()->new RuntimeException("Role Not Found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                        .username(userRegistrationDto.getUsername())
                        .email(userRegistrationDto.getEmail())
                        .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                        .roles(roles)
                        .build();

        userRepository.save(user);

        return UserDto.builder()
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .build();
    }

    @Override
    public UserDto login(UserLoginDto userLoginDto) {

        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(()-> new RuntimeException("User Not Found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), userLoginDto.getPassword()));


        return UserDto.builder()
                .email(user.getEmail())
                .token(jwtProvider.generateAccessToken(authentication))
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

    @Override
    public User getUser(Long id) {
        return null;
    }

}
