package com.clement.realworld.domain.user;

import com.clement.realworld.domain.user.dto.UserDto;
import com.clement.realworld.domain.user.dto.UserLoginDto;
import com.clement.realworld.domain.user.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDto register(UserRegistrationDto userRegistrationDto);
    UserDto login(UserLoginDto userLoginDto);
    User getUser(Long id);

}
