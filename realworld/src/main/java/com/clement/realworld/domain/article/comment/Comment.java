package com.clement.realworld.domain.article.comment;

import com.clement.realworld.domain.article.Article;
import com.clement.realworld.domain.common.BaseEntity;
import com.clement.realworld.domain.user.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @NotBlank
    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

}
