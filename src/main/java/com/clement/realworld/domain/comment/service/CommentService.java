package com.clement.realworld.domain.comment.service;

import com.clement.realworld.domain.comment.dto.CreateCommentDTO;
import com.clement.realworld.domain.comment.dto.MultipleCommentsDTO;
import com.clement.realworld.domain.comment.dto.SingleCommentDTO;

public interface CommentService {
    SingleCommentDTO addComment(String username, String slug, CreateCommentDTO createCommentDto);
    MultipleCommentsDTO getCommentsByArticleSlug(String username, String slug);
    void deleteComment(String username, String slug, Long id);
}
