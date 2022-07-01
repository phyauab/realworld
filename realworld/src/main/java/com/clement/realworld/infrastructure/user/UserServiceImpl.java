package com.clement.realworld.infrastructure.user;

import com.clement.realworld.application.security.JWTProvider;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.UserService;
import com.clement.realworld.domain.user.dto.UserDto;
import com.clement.realworld.domain.user.dto.UserLoginDto;
import com.clement.realworld.domain.user.dto.UserRegistrationDto;
import com.clement.realworld.domain.user.dto.UserUpdateDto;
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

        return convertToUserDto(user, null);
    }

    @Override
    public UserDto login(UserLoginDto userLoginDto) {

        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(()-> new RuntimeException("User Not Found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), userLoginDto.getPassword()));

        String accessToken = jwtProvider.generateAccessToken(authentication);

        return convertToUserDto(user, accessToken);
    }

    @Override
    public UserDto getCurrentUser(String username, String token) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        return convertToUserDto(user, token);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto, String username, String token) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        if(userUpdateDto.getEmail() != null)
            user.setEmail(userUpdateDto.getEmail());

        if(userUpdateDto.getUsername() != null)
            user.setUsername(userUpdateDto.getUsername());

        if(userUpdateDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));

        if(userUpdateDto.getImage() != null)
            user.setImage(userUpdateDto.getImage());

        if(userUpdateDto.getBio() != null)
            user.setBio(userUpdateDto.getBio());

        userRepository.save(user);

        return convertToUserDto(user, token);
    }

    private UserDto convertToUserDto(User user, String token) {
        return UserDto.builder()
                .email(user.getEmail())
                .token(token)
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

}
