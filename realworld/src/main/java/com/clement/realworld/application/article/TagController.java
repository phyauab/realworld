package com.clement.realworld.application.article;

import com.clement.realworld.domain.article.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private TagService tagService;

    @GetMapping
    public ResponseEntity<?> getTags() {
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }

}
