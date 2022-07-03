package com.clement.realworld.application.user;

import com.clement.realworld.domain.user.profile.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {

    private ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username, Principal principal){
        String currentUsername = principal == null ? null : principal.getName();
        return new ResponseEntity<>(profileService.getProfile(username, currentUsername), HttpStatus.OK);
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<?> followUser(@PathVariable String username, Principal principal) {
        return new ResponseEntity<>(profileService.followUser(username, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<?> unfollowUser(@PathVariable String username, Principal principal) {
        return new ResponseEntity<>(profileService.unfollowUser(username, principal.getName()), HttpStatus.OK);
    }

}
