package com.clement.realworld.domain.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListParam {

    private String tag;
    private String author;
    private String favorited;
    private int limit = 10;
    private int offset = 0;


}
