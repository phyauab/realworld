package com.clement.realworld.infrastructure.user;

import com.clement.realworld.application.exception.ValidationException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTProvider jwtProvider;

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

        user = userRepository.save(user);

        return convertToUserDto(user, jwtProvider.generateAccessToken(user.getUsername()));
    }

    @Override
    public UserDto login(UserLoginDto userLoginDto) {

        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(()-> new ValidationException("email or password", "is invalid"));

        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword()))
            throw new ValidationException("email or password", "is invalid");

        String accessToken = jwtProvider.generateAccessToken(user.getUsername());

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
