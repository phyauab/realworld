package com.clement.realworld.domain.article.comment;

import com.clement.realworld.domain.article.dto.AuthorDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SingleComment {

    private CommentDto comment;

}
