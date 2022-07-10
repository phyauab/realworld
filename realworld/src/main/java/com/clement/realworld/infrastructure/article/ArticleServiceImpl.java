package com.clement.realworld.infrastructure.article;

import com.clement.realworld.domain.article.Article;
import com.clement.realworld.domain.article.ArticleRepository;
import com.clement.realworld.domain.article.ArticleService;
import com.clement.realworld.domain.article.dto.ArticleDto;
import com.clement.realworld.domain.article.dto.AuthorDto;
import com.clement.realworld.domain.article.dto.CreateArticleDto;
import com.clement.realworld.domain.article.favorite.Favorite;
import com.clement.realworld.domain.article.tag.Tag;
import com.clement.realworld.domain.article.tag.TagRepository;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.follow.Follow;
import com.clement.realworld.domain.user.follow.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private TagRepository tagRepository;
    private FollowRepository followRepository;

    @Override
    @Transactional
    public ArticleDto createArticle(String username, CreateArticleDto createArticleDto) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        String slug = slugify(createArticleDto.getTitle());
        Set<Tag> tags = findOrBuildTagSet(createArticleDto.getTagList());

        Article article = Article.builder()
                .title(createArticleDto.getTitle())
                .slug(slug)
                .description(createArticleDto.getDescription())
                .author(user)
                .tags(tags)
                .build();

        articleRepository.save(article);

        return convertToDto(article, false, 0, false);
    }

    @Override
    public ArticleDto getArticle(String slug, String username) {

        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        return convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent());

    }

    private String slugify(String title) {
        List<String> latestSlug = articleRepository.findLatestSlug(title, PageRequest.of(0,1));
        String slugWithoutNumber = String.join("-", title.toLowerCase().split(" "));
        String slug;

        if(latestSlug.isEmpty()) {
            slug = slugWithoutNumber + "-1";
        } else {
            int lastNumber = Integer.parseInt(latestSlug.get(0).substring(slugWithoutNumber.length() + 1));
            slug = slugWithoutNumber + "-" + (lastNumber + 1);
        }

        return slug;
    }

    private ArticleDto convertToDto(Article article, boolean favorited, long favoriteCount, boolean following) {

        User author = article.getAuthor();

        AuthorDto authorDto = AuthorDto.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(following)
                .build();

        Set<String> tagList = article.getTags().stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toSet());

        return ArticleDto.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getDescription())
                .tagList(tagList)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(favorited)
                .favoritesCount(favoriteCount)
                .author(authorDto)
                .build();
    }

    private Set<Tag> findOrBuildTagSet(List<String> tagList) {
        Set<Tag> tagSet = new HashSet<>();
        for(String tagName : tagList) {
            Optional<Tag> tag = tagRepository.findByName(tagName);
            if(tag.isPresent()) {
                tagSet.add(tag.get());
                continue;
            }
            Tag newTag = new Tag(tagName);
            tagSet.add(newTag);
        }
        return tagSet;
    }

}
