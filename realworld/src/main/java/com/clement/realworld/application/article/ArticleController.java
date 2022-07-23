package com.clement.realworld.application.article;

import com.clement.realworld.domain.article.ArticleService;
import com.clement.realworld.domain.article.dto.ArticleDto;
import com.clement.realworld.domain.article.dto.ArticleListParam;
import com.clement.realworld.domain.article.dto.CreateArticleDto;
import com.clement.realworld.domain.article.dto.SingleArticle;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/articles")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<?> createArticle(Principal principal, @RequestBody CreateArticleDto createArticleDto) {
        return new ResponseEntity<>(articleService.createArticle(principal.getName(), createArticleDto), HttpStatus.CREATED);
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<?> favoriteArticle(Principal principal, @PathVariable("slug") String slug) {
        String username = principal == null ? null : principal.getName();
        return new ResponseEntity<>(articleService.favoriteArticle(username, slug), HttpStatus.OK);
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

}
