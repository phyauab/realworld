package com.clement.realworld.domain.profile.service;

import com.clement.realworld.domain.follow.Follow;
import com.clement.realworld.domain.follow.FollowRepository;
import com.clement.realworld.domain.profile.dto.ProfileDTO;
import com.clement.realworld.domain.user.entity.User;
import com.clement.realworld.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private UserRepository userRepository;
    private FollowRepository followRepository;

    @Override
    public ProfileDTO getProfile(String username, String currentUsername) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Boolean following = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername)
                                            .isPresent();

        return convertToProfileDto(user, following);
    }

    @Override
    @Transactional
    public ProfileDTO followUser(String username, String currentUsername) {

        Boolean following = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername).isPresent();
        if(following)
            throw new RuntimeException("You have already followed " + username);

        User followee = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        User follower = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User Not Found"));

        Follow follow = new Follow(followee, follower);
        followRepository.save(follow);

        return convertToProfileDto(followee, true);
    }

    @Override
    @Transactional
    public ProfileDTO unfollowUser(String username, String currentUsername) {

        Boolean following = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername).isPresent();
        if(!following)
            throw new RuntimeException("You have not followed " + username);

        User followee = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));

        Follow follow = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername)
                                        .orElseThrow(() -> new RuntimeException("Follow Not Found"));

        followRepository.delete(follow);

        return convertToProfileDto(followee, false);
    }

    private ProfileDTO convertToProfileDto(User user, Boolean following) {
        return ProfileDTO.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
