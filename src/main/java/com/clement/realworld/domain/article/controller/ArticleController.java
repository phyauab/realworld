package com.clement.realworld.domain.article.controller;

import com.clement.realworld.domain.article.service.ArticleService;
import com.clement.realworld.domain.comment.service.CommentService;
import com.clement.realworld.domain.comment.dto.CreateCommentDTO;
import com.clement.realworld.domain.article.dto.*;
import com.clement.realworld.domain.common.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/articles")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createArticle(Principal principal, @Validated @RequestBody CreateArticleDTO createArticleDto) {
        return new ResponseEntity<>(articleService.createArticle(principal.getName(), createArticleDto), HttpStatus.CREATED);
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<?> favoriteArticle(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.favoriteArticle(username, slug), HttpStatus.OK);
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<?> addComment(Principal principal,
                                        @PathVariable("slug") String slug,
                                        @RequestBody CreateCommentDTO createCommentDto) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(commentService.addComment(username, slug, createCommentDto), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getArticle(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.getArticle(slug, username) ,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getArticles(Principal principal, ArticleListParam articleListParam) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.listArticles(username, articleListParam), HttpStatus.OK);
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getFeedArticles(Principal principal, Pagination pagination) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.feedArticles(username, pagination), HttpStatus.OK);
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<?> getComments(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(commentService.getCommentsByArticleSlug(username, slug), HttpStatus.OK);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<?> updateArticle(Principal principal,
                                           @PathVariable("slug") String slug,
                                           @Validated @RequestBody UpdateArticleDTO updateArticleDto) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.updateArticle(username, slug, updateArticleDto), HttpStatus.OK);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteArticle(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        articleService.deleteArticle(username, slug);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{slug}/comments/{id}")
    public ResponseEntity<?> deleteComment(Principal principal,
                                           @PathVariable("slug") String slug,
                                           @PathVariable("id") Long id){
        String username = principal == null ? null : principal.getName();
        commentService.deleteComment(username, slug, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<?> unfavoriteArticle(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.unfavoriteArticle(username, slug), HttpStatus.OK);
    }



}
