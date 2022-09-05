package com.clement.realworld.domain.article.service;

import com.clement.realworld.domain.comment.entity.Comment;
import com.clement.realworld.domain.comment.repository.CommentRepository;
import com.clement.realworld.domain.article.dto.*;
import com.clement.realworld.domain.article.entity.Article;
import com.clement.realworld.domain.favorite.entity.Favorite;
import com.clement.realworld.domain.favorite.repository.FavoriteRepository;
import com.clement.realworld.domain.article.repository.ArticleRepository;
import com.clement.realworld.domain.tag.entity.Tag;
import com.clement.realworld.domain.tag.repository.TagRepository;
import com.clement.realworld.domain.common.Pagination;
import com.clement.realworld.domain.user.entity.User;
import com.clement.realworld.domain.user.repository.UserRepository;
import com.clement.realworld.domain.follow.Follow;
import com.clement.realworld.domain.follow.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private TagRepository tagRepository;
    private FollowRepository followRepository;
    private FavoriteRepository favoriteRepository;
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public SingleArticleDTO createArticle(String username, CreateArticleDTO createArticleDto) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        String slug = slugify(createArticleDto.getTitle());
        Set<Tag> tags = findOrBuildTagSet(createArticleDto.getTagList());

        Article article = Article.builder()
                .title(createArticleDto.getTitle())
                .slug(slug)
                .description(createArticleDto.getDescription())
                .body(createArticleDto.getBody())
                .author(user)
                .tags(tags)
                .build();

        articleRepository.save(article);

        return new SingleArticleDTO(convertToDto(article, false, 0, false));
    }

    @Override
    public SingleArticleDTO getArticle(String slug, String username) {

        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.isPresent() ? user.get().getId() : null;
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(userId, article.getId());
        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        return new SingleArticleDTO(convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent()));

    }

    @Override
    public MultipleArticlesDTO listArticles(String username, ArticleListParam articleListParam) {
        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.isPresent() ? user.get().getId() : null;

        Pageable pageable = null;
        if(articleListParam.getOffset() != null && articleListParam.getLimit() != null) {
            pageable = PageRequest.of(articleListParam.getOffset() / articleListParam.getLimit(),
                                            articleListParam.getLimit());
        }
        List<Article> articles = articleRepository.findAll(articleListParam.getFavorited(),
                                                            articleListParam.getAuthor(),
                                                            articleListParam.getTag(),
                                                            pageable);
        List<ArticleDTO> articleDTOS = new ArrayList<>();

        for(Article article : articles) {
            Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(userId, article.getId());
            long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
            Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);
            ArticleDTO articleDto = convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent());
            articleDTOS.add(articleDto);
        }

        return new MultipleArticlesDTO(articleDTOS, articleDTOS.size());
    }

    @Override
    public MultipleArticlesDTO feedArticles(String username, Pagination pagination) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Pageable pageable = null;
        if(pagination.getOffset() != null && pagination.getLimit() != null) {
            pageable = PageRequest.of(pagination.getOffset() / pagination.getLimit(),
                    pagination.getLimit());
        }
        List<Article> articles = articleRepository.findFeedArticles(username, pageable);
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for(Article article : articles) {
            Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
            long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
            Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);
            ArticleDTO articleDto = convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent());
            articleDTOS.add(articleDto);
        }
        return new MultipleArticlesDTO(articleDTOS, articleDTOS.size());
    }

    @Override
    public SingleArticleDTO updateArticle(String username, String slug, UpdateArticleDTO updateArticleDto) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));

        if(updateArticleDto.getTitle() != null && !article.getTitle().contentEquals(updateArticleDto.getTitle())) {
            article.setTitle(updateArticleDto.getTitle());
            String newSlug = slugify(updateArticleDto.getTitle());
            article.setSlug(newSlug);
        }

        if(updateArticleDto.getDescription() != null)
            article.setDescription(updateArticleDto.getDescription());

        if(updateArticleDto.getBody() != null)
            article.setBody(updateArticleDto.getBody());

        Article updatedArticle = articleRepository.save(article);
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);
        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());

        return new SingleArticleDTO(convertToDto(updatedArticle, favorite.isPresent(), favoriteCount, follow.isPresent()));
    }

    @Override
    @Transactional
    public void deleteArticle(String username, String slug) {

        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        if(!article.getAuthor().getUsername().contentEquals(username))
            throw new RuntimeException("Only the author is allowed to delete the article");

        List<Comment> comments = commentRepository.findByArticleSlug(slug, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Favorite> favorites = favoriteRepository.findByArticleSlug(slug);

        commentRepository.deleteAll(comments);
        favoriteRepository.deleteAll(favorites);
        article.removeAllTags();
        articleRepository.delete(article);
    }

    @Override
    public SingleArticleDTO favoriteArticle(String username, String slug) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        if(favorite.isEmpty()) {
            Favorite newFavorite = new Favorite(user, article);
            favoriteRepository.save(newFavorite);
        }

        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());

        return new SingleArticleDTO(convertToDto(article, true, favoriteCount, follow.isPresent()));
    }

    @Override
    public SingleArticleDTO unfavoriteArticle(String username, String slug) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        if(favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
        }

        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());

        return new SingleArticleDTO(convertToDto(article, false, favoriteCount, follow.isPresent()));
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

    private ArticleDTO convertToDto(Article article, boolean favorited, long favoriteCount, boolean following) {

        User author = article.getAuthor();

        AuthorDTO authorDto = AuthorDTO.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(following)
                .build();

        Set<String> tagList = null;
        if(article.getTags() != null) {
            tagList = article.getTags().stream()
                                        .map(tag -> tag.getName())
                                        .collect(Collectors.toSet());
        }

        return ArticleDTO.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(tagList)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(favorited)
                .favoritesCount(favoriteCount)
                .author(authorDto)
                .build();
    }

    private Set<Tag> findOrBuildTagSet(List<String> tagList) {

        if(tagList == null) return null;

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
