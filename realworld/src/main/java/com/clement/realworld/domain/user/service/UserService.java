package com.clement.realworld.domain.user.service;

import com.clement.realworld.domain.user.dto.UserDTO;
import com.clement.realworld.domain.user.dto.LoginDTO;
import com.clement.realworld.domain.user.dto.RegistrationDTO;
import com.clement.realworld.domain.user.dto.UserUpdateDTO;

public interface UserService {

    UserDTO register(RegistrationDTO registrationDto);
    UserDTO login(LoginDTO loginDTO);
    UserDTO getCurrentUser(String username, String token);
    UserDTO updateUser(UserUpdateDTO userUpdateDto, String username, String token);

}
