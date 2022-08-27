package com.clement.realworld.application.user;


import com.clement.realworld.domain.user.UserService;
import com.clement.realworld.domain.user.dto.UserDto;
import com.clement.realworld.domain.user.dto.UserLoginDto;
import com.clement.realworld.domain.user.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return new ResponseEntity<>(userService.register(userRegistrationDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return new ResponseEntity<>(userService.login(userLoginDto), HttpStatus.OK);
    }

}
