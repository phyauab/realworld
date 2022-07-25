package com.clement.realworld.domain.article.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "WHERE c.id = :id" +
            "   AND c.article.slug = :slug")
    Optional<Comment> findByIdAndArticleSlug(@Param("id") Long id, @Param("slug") String slug);

    List<Comment> findByArticleSlug(String slug);
}
