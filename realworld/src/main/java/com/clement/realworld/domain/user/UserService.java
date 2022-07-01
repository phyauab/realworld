package com.clement.realworld.domain.user;

import com.clement.realworld.domain.user.dto.UserDto;
import com.clement.realworld.domain.user.dto.UserLoginDto;
import com.clement.realworld.domain.user.dto.UserRegistrationDto;
import com.clement.realworld.domain.user.dto.UserUpdateDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDto register(UserRegistrationDto userRegistrationDto);
    UserDto login(UserLoginDto userLoginDto);
    UserDto getCurrentUser(String username, String token);
    UserDto updateUser(UserUpdateDto userUpdateDto, String username, String token);

}
