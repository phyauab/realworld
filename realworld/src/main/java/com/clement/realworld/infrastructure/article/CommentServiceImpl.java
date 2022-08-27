package com.clement.realworld.infrastructure.article;

import com.clement.realworld.domain.article.Article;
import com.clement.realworld.domain.article.ArticleRepository;
import com.clement.realworld.domain.article.comment.*;
import com.clement.realworld.domain.article.dto.AuthorDto;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.follow.Follow;
import com.clement.realworld.domain.user.follow.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public MultipleComments getCommentsByArticleSlug(String username, String slug) {

        articleRepository.findBySlug(slug).orElseThrow(()-> new RuntimeException("Article Not Found"));

        List<Comment> comments = commentRepository.findByArticleSlug(slug, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<CommentDto> commentDtos = new ArrayList<>();
        for(Comment comment : comments) {
            Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(comment.getAuthor().getUsername(), username);
            CommentDto commentDto = convertToDto(comment, follow.isPresent());
            commentDtos.add(commentDto);
        }

        return new MultipleComments(commentDtos);
    }

    @Override
    public void deleteComment(String username, String slug, Long id) {

        boolean exists = userRepository.existsByUsername(username);
        if(!exists)
            throw new RuntimeException("User Not Found");

        Comment comment = commentRepository.findByIdAndArticleSlug(id, slug).orElseThrow(() -> new RuntimeException("Comment Not Found"));

        if(!comment.getAuthor().getUsername().contentEquals(username))
            throw new RuntimeException("Only the author is allowed to delete the comment");

        commentRepository.delete(comment);
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
