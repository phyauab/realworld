package com.clement.realworld.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ArticleDto {

    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<String> tagList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean favorited;
    private long favoritesCount;
    private AuthorDto author;

}
