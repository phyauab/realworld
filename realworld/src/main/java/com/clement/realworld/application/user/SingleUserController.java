package com.clement.realworld.application.user;

import com.clement.realworld.domain.user.UserService;
import com.clement.realworld.domain.user.dto.UserUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class SingleUserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getCurrentUser(Principal principal, @RequestHeader(name="Authorization") String token) {
        return new ResponseEntity<>(
                userService.getCurrentUser(
                        principal.getName(),
                        token.substring("Token ".length())),
                HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto, Principal principal, @RequestHeader(name="Authorization") String token) {
        return new ResponseEntity<>(
                userService.updateUser(
                        userUpdateDto,
                        principal.getName(),
                        token.substring("Token ".length())),
                HttpStatus.OK);
    }

}
