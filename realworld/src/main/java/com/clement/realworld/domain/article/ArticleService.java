package com.clement.realworld.domain.article;

import com.clement.realworld.domain.article.dto.*;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;

@Repository
public interface ArticleService {

    SingleArticle createArticle(String username, CreateArticleDto createArticleDto);
    SingleArticle getArticle(String slug, String username);
    MultipleArticles listArticles(String username, ArticleListParam articleListParam);
    SingleArticle favoriteArticle(String username, String slug);

}
