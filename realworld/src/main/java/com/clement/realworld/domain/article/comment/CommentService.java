package com.clement.realworld.domain.article.comment;

public interface CommentService {
    SingleComment addComment(String username, String slug, CreateCommentDto createCommentDto);
    void deleteComment(String username, String slug, Long id);
}
