package com.clement.realworld.domain.profile.service;

import com.clement.realworld.domain.profile.dto.ProfileDTO;

public interface ProfileService {

    ProfileDTO getProfile(String username, String currentUsername);
    ProfileDTO followUser(String username, String currentUsername);
    ProfileDTO unfollowUser(String username, String currentUsername);

}
