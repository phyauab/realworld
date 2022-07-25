package com.clement.realworld.domain.article.comment;

import java.util.List;

public interface CommentService {
    SingleComment addComment(String username, String slug, CreateCommentDto createCommentDto);
    MultipleComments getCommentsByArticleSlug(String username, String slug);
    void deleteComment(String username, String slug, Long id);
}
