package com.clement.realworld.infrastructure.article;

import com.clement.realworld.domain.article.tag.TagRepository;
import com.clement.realworld.domain.article.tag.TagService;
import com.clement.realworld.domain.article.tag.TagsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Override
    public TagsDto getTags() {
        List<String> tags = tagRepository.findAllTagNames();
        TagsDto tagsDto = new TagsDto();
        tagsDto.setTags(tags);
        return tagsDto;
    }
}
