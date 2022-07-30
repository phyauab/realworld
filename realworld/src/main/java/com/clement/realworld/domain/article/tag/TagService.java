package com.clement.realworld.domain.article.tag;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagService {

    TagsDto getTags();

}
