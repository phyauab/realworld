package com.clement.realworld.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFolloweeUsernameAndFollowerUsername(String followeeName, String followerName);

}
