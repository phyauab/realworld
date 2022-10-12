package com.clement.realworld.domain.tag.repository;

import com.clement.realworld.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT t.name FROM Tag t")
    List<String> findAllTagNames();

}
