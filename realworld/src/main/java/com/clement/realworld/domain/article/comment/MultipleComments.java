package com.clement.realworld.domain.article.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class MultipleComments {

    private List<CommentDto> comments;
}
