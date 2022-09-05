package com.clement.realworld.domain.article.service;

import com.clement.realworld.domain.article.dto.*;
import com.clement.realworld.domain.common.Pagination;

public interface ArticleService {

    SingleArticleDTO createArticle(String username, CreateArticleDTO createArticleDto);
    SingleArticleDTO getArticle(String slug, String username);
    MultipleArticlesDTO listArticles(String username, ArticleListParam articleListParam);
    MultipleArticlesDTO feedArticles(String username, Pagination pagination);
    SingleArticleDTO updateArticle(String username, String slug, UpdateArticleDTO updateArticleDto);
    void deleteArticle(String username, String slug);
    SingleArticleDTO favoriteArticle(String username, String slug);
    SingleArticleDTO unfavoriteArticle(String username, String slug);

}
