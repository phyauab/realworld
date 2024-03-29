package com.clement.realworld.domain.article.entity;

import com.clement.realworld.domain.tag.entity.Tag;
import com.clement.realworld.domain.common.BaseEntity;
import com.clement.realworld.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(unique = true)
    private String slug;

    @NotBlank
    private String description;

    @NotBlank
    private String body;

    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Tag> tags;

    public void removeAllTags() {
        for(Tag tag : tags) {
            tag.getArticles().remove(this);
        }
        tags.removeAll(tags);
    }

}
