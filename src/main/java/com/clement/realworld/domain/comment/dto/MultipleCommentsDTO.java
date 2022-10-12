package com.clement.realworld.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class MultipleCommentsDTO {

    private List<CommentDTO> comments;

}
