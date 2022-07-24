package com.clement.realworld.infrastructure.article;

import com.clement.realworld.domain.article.Article;
import com.clement.realworld.domain.article.ArticleRepository;
import com.clement.realworld.domain.article.comment.*;
import com.clement.realworld.domain.article.dto.AuthorDto;
import com.clement.realworld.domain.article.favorite.Favorite;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.follow.Follow;
import com.clement.realworld.domain.user.follow.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;
    private FollowRepository followRepository;

    @Override
    public SingleComment addComment(String username, String slug, CreateCommentDto createCommentDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        Comment comment = Comment.builder()
                .article(article)
                .body(createCommentDto.getBody())
                .author(user)
                .build();

        commentRepository.save(comment);

        return new SingleComment(convertToDto(comment, follow.isPresent()));
    }

    private CommentDto convertToDto(Comment comment, boolean following) {
        User author = comment.getAuthor();

        AuthorDto authorDto = AuthorDto.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(following)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(authorDto)
                .build();

        return commentDto;
    }
}
