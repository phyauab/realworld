package com.clement.realworld.application.article;

import com.clement.realworld.domain.article.ArticleService;
import com.clement.realworld.domain.article.dto.ArticleDto;
import com.clement.realworld.domain.article.dto.CreateArticleDto;
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

    @GetMapping("/{slug}")
    public ResponseEntity<?> getArticle(Principal principal, @PathVariable("slug") String slug) {
        return new ResponseEntity<>(articleService.getArticle(slug, principal.getName()), HttpStatus.OK);
    }

}
