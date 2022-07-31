package com.clement.realworld.domain.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListParam {

    private String tag;
    private String author;
    private String favorited;
    private Integer limit;
    private Integer offset;


}
