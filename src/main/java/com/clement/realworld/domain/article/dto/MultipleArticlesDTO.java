package com.clement.realworld.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class MultipleArticlesDTO {

    private List<ArticleDTO> articles;
    private int articlesCount;

}
