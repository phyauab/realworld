package com.clement.realworld.domain.article;

import com.clement.realworld.domain.article.dto.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleService {

    SingleArticle createArticle(String username, CreateArticleDto createArticleDto);
    SingleArticle getArticle(String slug, String username);
    MultipleArticles listArticles(String username, ArticleListParam articleListParam);
    SingleArticle updateArticle(String username, String slug, UpdateArticleDto updateArticleDto);
    SingleArticle favoriteArticle(String username, String slug);
    SingleArticle unfavoriteArticle(String username, String slug);

}
