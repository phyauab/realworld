package com.clement.realworld.domain.article.comment;

import com.clement.realworld.domain.article.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private AuthorDto author;

}
