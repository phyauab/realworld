package com.clement.realworld.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDTO {

    private String username;
    private String bio;
    private String image;
    private boolean following;

}
