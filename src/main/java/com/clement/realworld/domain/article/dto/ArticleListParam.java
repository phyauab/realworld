package com.clement.realworld.domain.article.dto;

import com.clement.realworld.domain.common.Pagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListParam extends Pagination {

    private String tag;
    private String author;
    private String favorited;

}
