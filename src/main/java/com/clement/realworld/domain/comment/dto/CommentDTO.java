package com.clement.realworld.domain.comment.dto;

import com.clement.realworld.domain.article.dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDTO {

    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private AuthorDTO author;

}
