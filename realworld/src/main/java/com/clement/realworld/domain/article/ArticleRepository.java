package com.clement.realworld.domain.article;

import com.clement.realworld.domain.article.favorite.Favorite;
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

    @Query("SELECT DISTINCT a FROM Article a " +
            "LEFT JOIN a.author au " +
            "LEFT JOIN Favorite f ON f.article = a " +
            "LEFT JOIN f.user u " +
            "LEFT JOIN a.tags t " +
            "WHERE (:favorited IS NULL OR u.username = :favorited)" +
            "   AND (:author IS NULL OR au.username = :author)" +
            "   AND (:tag IS NULL OR :tag IN t.name)" +
            "ORDER BY a.createdAt desc")
    List<Article> findAll(@Param("favorited") String favorited,
                          @Param("author") String author,
                          @Param("tag") String tag,
                          Pageable pageable);

    @Query("SELECT DISTINCT a FROM Article a " +
            "JOIN a.author au " +
            "JOIN Follow f ON f.followee = au " +
            "WHERE f.follower.username = :username")
    List<Article> findFeedArticles(@Param("username") String username, Pageable pageable);

    Optional<Article> findBySlug(String slug);



}
