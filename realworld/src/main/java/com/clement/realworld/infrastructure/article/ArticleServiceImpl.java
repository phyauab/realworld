package com.clement.realworld.infrastructure.article;

import com.clement.realworld.domain.article.Article;
import com.clement.realworld.domain.article.ArticleRepository;
import com.clement.realworld.domain.article.ArticleService;
import com.clement.realworld.domain.article.comment.CreateCommentDto;
import com.clement.realworld.domain.article.comment.SingleComment;
import com.clement.realworld.domain.article.dto.*;
import com.clement.realworld.domain.article.favorite.Favorite;
import com.clement.realworld.domain.article.favorite.FavoriteRepository;
import com.clement.realworld.domain.article.tag.Tag;
import com.clement.realworld.domain.article.tag.TagRepository;
import com.clement.realworld.domain.user.User;
import com.clement.realworld.domain.user.UserRepository;
import com.clement.realworld.domain.user.follow.Follow;
import com.clement.realworld.domain.user.follow.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Override
    @Transactional
    public SingleArticle createArticle(String username, CreateArticleDto createArticleDto) {

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

        return new SingleArticle(convertToDto(article, false, 0, false));
    }

    @Override
    public SingleArticle getArticle(String slug, String username) {

        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.isPresent() ? user.get().getId() : null;
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(userId, article.getId());
        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        return new SingleArticle(convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent()));

    }

    @Override
    public MultipleArticles listArticles(String username, ArticleListParam articleListParam) {
        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.isPresent() ? user.get().getId() : null;

        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> articleDtos = new ArrayList<>();

        for(Article article : articles) {
            Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(userId, article.getId());
            long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());
            Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);
            ArticleDto articleDto = convertToDto(article, favorite.isPresent(), favoriteCount, follow.isPresent());
            articleDtos.add(articleDto);
        }

        return new MultipleArticles(articleDtos, articleDtos.size());
    }

    @Override
    public SingleArticle updateArticle(String username, String slug, UpdateArticleDto updateArticleDto) {

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

        return new SingleArticle(convertToDto(updatedArticle, favorite.isPresent(), favoriteCount, follow.isPresent()));
    }

    @Override
    public SingleArticle favoriteArticle(String username, String slug) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        if(favorite.isEmpty()) {
            Favorite newFavorite = new Favorite(user, article);
            favoriteRepository.save(newFavorite);
        }

        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());

        return new SingleArticle(convertToDto(article, true, favoriteCount, follow.isPresent()));
    }

    @Override
    public SingleArticle unfavoriteArticle(String username, String slug) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Article Not Found"));
        Optional<Favorite> favorite = articleRepository.findFavoriteByUserIdAndArticleId(user.getId(), article.getId());
        Optional<Follow> follow = followRepository.findByFolloweeUsernameAndFollowerUsername(article.getAuthor().getUsername(), username);

        if(favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
        }

        long favoriteCount = articleRepository.findFavoriteCountByArticleId(article.getId());

        return new SingleArticle(convertToDto(article, false, favoriteCount, follow.isPresent()));
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
