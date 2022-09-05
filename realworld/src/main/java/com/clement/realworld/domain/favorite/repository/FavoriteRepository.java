package com.clement.realworld.domain.favorite.repository;

import com.clement.realworld.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByArticleSlug(String slug);
}
