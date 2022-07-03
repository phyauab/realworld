package com.clement.realworld.infrastructure.user;

import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.follow.Follow;
import com.clement.realworld.domain.user.follow.FollowRepository;
import com.clement.realworld.domain.user.profile.ProfileDto;
import com.clement.realworld.domain.user.profile.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private UserRepository userRepository;
    private FollowRepository followRepository;

    @Override
    public ProfileDto getProfile(String username, String currentUsername) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Boolean following = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername)
                                            .isPresent();

        return convertToProfileDto(user, following);
    }

    @Override
    @Transactional
    public ProfileDto followUser(String username, String currentUsername) {

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
    public ProfileDto unfollowUser(String username, String currentUsername) {

        Boolean following = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername).isPresent();
        if(!following)
            throw new RuntimeException("You have not followed " + username);

        User followee = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        User follower = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User Not Found"));

        Follow follow = followRepository.findByFolloweeUsernameAndFollowerUsername(username, currentUsername)
                                        .orElseThrow(() -> new RuntimeException("Follow Not Found"));

        followRepository.delete(follow);

        return convertToProfileDto(followee, false);
    }

    private ProfileDto convertToProfileDto(User user, Boolean following) {
        return ProfileDto.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
