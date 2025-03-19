package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.Image;
import com.fanzehao.blogsystem.model.Tag;
import com.fanzehao.blogsystem.repository.ArticleRepository;
import com.fanzehao.blogsystem.repository.ImageRepository;
import com.fanzehao.blogsystem.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageRepository imageRepository;

    //判断标签存不存在并且初始化标签


    public Article createArticle(Article article) {
        // 生成文章摘要如果内容超过200字就截取前200字 没有的话就使用前端发送的摘要
        if (article.getContent() != null && article.getContent().length() > 200) {
            article.setSummary(article.getContent().substring(0, 200) + "...");
        }

        return articleRepository.save(article);
    }

    public Page<Article> findAllArticles(Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return articleRepository.findAll(pageable);

    }

    public Boolean deleteArticleById(Long id) {
        try {
            articleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("删除失败");
            e.printStackTrace();

            return false;
        }
    }
    public Boolean updateAriticleById(Long id, Article article) {
        try {
            // 根据id查找文章
            Optional<Article> optionalArticle = articleRepository.findById(id);
            if (optionalArticle.isPresent()) {
                Article existingArticle = optionalArticle.get();
                // 更新文章的属性
                existingArticle.setTitle(article.getTitle());
                String newContent = article.getContent();
                existingArticle.setCategory(article.getCategory());
                Set<Image> imagesToRemove = existingArticle.getImages().stream()
                        .filter(image -> !newContent.contains(image.getUrl()))
                        .collect(Collectors.toSet());
                // 删除不再使用的图片从磁盘中
                imagesToRemove.forEach(image -> {
                    try {
                        Files.deleteIfExists(Paths.get(image.getPath()));
                        imageRepository.delete(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete image file: " + e.getMessage());
                    }
                });
                existingArticle.setContent(article.getContent());
                // 设置更新时间
                existingArticle.setUpdateTime(LocalDateTime.now());
                // 保存更新后的文章
                articleRepository.save(existingArticle);
                return true;
            } else {
                // 如果没有找到文章，返回false
                return false;
            }
        } catch (Exception e) {
            System.out.println("更新失败");
            e.printStackTrace();
            return false;
        }
    }

    public Page<Article> findUncategorizedArticles(Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;

        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return articleRepository.findByCategoryIdIsNull(pageable);
    }
    public Set<Tag> findtagsSetByArticleId(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found")).getTagsSet();
    }

    public Article findArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public void updateViewCount(Long articleId, Integer viewCount) {
        articleRepository.incrementViewCount(articleId, viewCount);
    }

    public Page<Article> findArticlesByCategoryId(Long id, Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;

        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return articleRepository.findByCategoryId(id, pageable);
    }


    public Page<Article> findArticlesByTagId(Long tagId, Integer page, Integer pageSize) {
        if (page == null || page < 1) {

            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return articleRepository.findByTagsSet_id(tagId, pageable);
    }

    public Long countAllArticles() {
        return articleRepository.countAllArticles();
    }

    public Result<?> getTagsByArticleId(Long id) {
        return Result.success(findtagsSetByArticleId(id));
    }
}
