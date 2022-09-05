package com.clement.realworld.domain.tag.service;

import com.clement.realworld.domain.tag.repository.TagRepository;
import com.clement.realworld.domain.tag.dto.TagsDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Override
    public TagsDTO getTags() {
        List<String> tags = tagRepository.findAllTagNames();
        TagsDTO tagsDto = new TagsDTO();
        tagsDto.setTags(tags);
        return tagsDto;
    }
}
