package com.clement.realworld.domain.tag.controller;

import com.clement.realworld.domain.tag.service.TagService;
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
