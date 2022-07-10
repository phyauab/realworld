package com.clement.realworld.domain.article;

import com.clement.realworld.domain.article.favorite.Favorite;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a.slug FROM Article a WHERE a.title = :title ORDER BY a.createdAt DESC")
    List<String> findLatestSlug(@Param("title") String title, Pageable pageable);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.article.id = :articleId")
    Optional<Favorite> findFavoriteByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.article.id = :articleId")
    long findFavoriteCountByArticleId(@Param("articleId") Long articleId);

    Optional<Article> findBySlug(String slug);



}
