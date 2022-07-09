package com.clement.realworld.domain.article;

import com.clement.realworld.domain.article.dto.ArticleDto;
import com.clement.realworld.domain.article.dto.CreateArticleDto;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleService {

    ArticleDto createArticle(String username, CreateArticleDto createArticleDto);

}
