package com.clement.realworld.domain.comment.service;

import com.clement.realworld.domain.article.dto.AuthorDTO;
import com.clement.realworld.domain.article.entity.Article;
import com.clement.realworld.domain.article.repository.ArticleRepository;
import com.clement.realworld.domain.comment.repository.CommentRepository;
import com.clement.realworld.domain.comment.dto.CreateCommentDTO;
import com.clement.realworld.domain.comment.dto.MultipleCommentsDTO;
import com.clement.realworld.domain.comment.dto.SingleCommentDTO;
import com.clement.realworld.domain.comment.dto.CommentDTO;
import com.clement.realworld.domain.comment.entity.Comment;
import com.clement.realworld.domain.user.entity.User;
import com.clement.realworld.domain.user.repository.UserRepository;
import com.clement.realworld.domain.follow.Follow;
import com.clement.realworld.domain.follow.FollowRepository;
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
    public SingleCommentDTO addComment(String username, String slug, CreateCommentDTO createCommentDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        Comment comment = Comment.builder()
                .article(article)
                .body(createCommentDto.getBody())
                .author(user)
                .build();

        commentRepository.save(comment);

        return new SingleCommentDTO(convertToDto(comment, follow.isPresent()));
    }

    @Override
    public MultipleCommentsDTO getCommentsByArticleSlug(String username, String slug) {

        articleRepository.findBySlug(slug).orElseThrow(()-> new RuntimeException("Article Not Found"));

        List<Comment> comments = commentRepository.findByArticleSlug(slug, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment : comments) {
            Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(comment.getAuthor().getUsername(), username);
            CommentDTO commentDto = convertToDto(comment, follow.isPresent());
            commentDTOS.add(commentDto);
        }

        return new MultipleCommentsDTO(commentDTOS);
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

    private CommentDTO convertToDto(Comment comment, boolean following) {
        User author = comment.getAuthor();

        AuthorDTO authorDto = AuthorDTO.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(following)
                .build();

        CommentDTO commentDto = CommentDTO.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(authorDto)
                .build();

        return commentDto;
    }
}
