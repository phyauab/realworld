package com.clement.realworld.domain.user.controller;


import com.clement.realworld.domain.user.service.UserService;
import com.clement.realworld.domain.user.dto.UserDTO;
import com.clement.realworld.domain.user.dto.LoginDTO;
import com.clement.realworld.domain.user.dto.RegistrationDTO;
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
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationDTO registrationDto) {
        return new ResponseEntity<>(userService.register(registrationDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.login(loginDTO), HttpStatus.OK);
    }

}
