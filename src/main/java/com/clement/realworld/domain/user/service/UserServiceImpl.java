package com.clement.realworld.domain.user.service;

import com.clement.realworld.domain.user.repository.UserRepository;
import com.clement.realworld.domain.user.entity.User;
import com.clement.realworld.exception.ValidationException;
import com.clement.realworld.security.JwtProvider;
import com.clement.realworld.domain.user.dto.UserDTO;
import com.clement.realworld.domain.user.dto.LoginDTO;
import com.clement.realworld.domain.user.dto.RegistrationDTO;
import com.clement.realworld.domain.user.dto.UserUpdateDTO;
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
    private JwtProvider jwtProvider;

    @Override
    public UserDTO register(RegistrationDTO registrationDto) {

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                        .orElseThrow(()->new RuntimeException("Role Not Found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                        .username(registrationDto.getUsername())
                        .email(registrationDto.getEmail())
                        .password(passwordEncoder.encode(registrationDto.getPassword()))
                        .roles(roles)
                        .build();

        user = userRepository.save(user);

        return convertToUserDto(user, jwtProvider.generateAccessToken(user.getUsername()));
    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(()-> new ValidationException("email or password", "is invalid"));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
            throw new ValidationException("email or password", "is invalid");

        String accessToken = jwtProvider.generateAccessToken(user.getUsername());

        return convertToUserDto(user, accessToken);
    }

    @Override
    public UserDTO getCurrentUser(String username, String token) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        return convertToUserDto(user, token);
    }

    @Override
    public UserDTO updateUser(UserUpdateDTO userUpdateDto, String username, String token) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        if(userUpdateDto.getEmail() != null)
            user.setEmail(userUpdateDto.getEmail());

        if(userUpdateDto.getUsername() != null) {
            user.setUsername(userUpdateDto.getUsername());
            token = jwtProvider.generateAccessToken(user.getUsername());
        }

        if(userUpdateDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));

        if(userUpdateDto.getImage() != null)
            user.setImage(userUpdateDto.getImage());

        if(userUpdateDto.getBio() != null)
            user.setBio(userUpdateDto.getBio());

        userRepository.save(user);

        return convertToUserDto(user, token);
    }

    private UserDTO convertToUserDto(User user, String token) {
        return UserDTO.builder()
                .email(user.getEmail())
                .token(token)
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

}
