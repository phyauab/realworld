package com.clement.realworld.domain.user.profile;

public interface ProfileService {

    ProfileDto getProfile(String username, String currentUsername);
    ProfileDto followUser(String username, String currentUsername);
    ProfileDto unfollowUser(String username, String currentUsername);

}
