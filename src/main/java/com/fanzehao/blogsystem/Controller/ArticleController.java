package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.AlgoliaService;
import com.fanzehao.blogsystem.Service.ArticleService;
import com.fanzehao.blogsystem.Service.CategoryService;
import com.fanzehao.blogsystem.Service.TagService;
import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.Category;
import com.fanzehao.blogsystem.model.Image;
import com.fanzehao.blogsystem.model.Tag;
import com.fanzehao.blogsystem.repository.ImageRepository;
import com.fanzehao.blogsystem.response.ArticleResponse;
import com.fanzehao.blogsystem.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/articles")

public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AlgoliaService algoliaService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String ARTICLE_VIEW_COUNT_KEY = "article:view:count";

    //如果是windows系统用此url
    private final static String U = "http://localhost:8090";
    //如果是linux系统用此url,会使用nginx代理到http://localhost:8090
    private  final static String U2 = "www.fzhblog.cn";
    private static final Logger logger = Logger.getLogger(ArticleController.class.getName());
    @Autowired
    private TagService tagService;



    @Value("${temp.directory}")
    private String tempDirectory;

    @Value("${img.directory}")
    private String uploadDirectory;

    @Autowired
    private CategoryService categoryService;

    //根据文章id获取对应的所有标签
    @GetMapping("/{id}/tags")
    public Result<?> getTagsByArticleId(@PathVariable Long id) {
        return articleService.getTagsByArticleId(id);
    }

    /*
    "http://localhost:8090/api/articles",
            {
              title: this.article.title,
              content: this.article.content,
              tags: this.article.tags.map(tag => tag.text),
              categoryId: this.article.categoryId,
              summary: this.article.summary,
              isPublished: this.article.isPublished,
            }
     */
    @GetMapping("/{id}")
    public Result<?> getArticleById(@PathVariable Long id) {


        redisTemplate.opsForHash().increment(ARTICLE_VIEW_COUNT_KEY, id.toString(), 1);

        // 1. 从 Redis 获取文章的浏览量
        String viewCount = (String) redisTemplate.opsForHash().get(ARTICLE_VIEW_COUNT_KEY, id.toString());
        logger.info("文章ID: " + id + ", 浏览量: " + viewCount);
        // 2. 如果缓存中没有，则从数据库中查询
        if (viewCount == null) {
            Article articleById = articleService.findArticleById(id);
            return Result.success(articleById); // 这里返回文章信息时，也可以返回缓存的浏览量
        } else {
            // 3. 如果缓存中有，直接返回缓存中的浏览量
            Article articleById = articleService.findArticleById(id);
            articleById.setViews(Integer.parseInt(viewCount)); // 使用缓存中的浏览量更新文章对象
            return Result.success(articleById);
        }
    }


    public Article setArticle(Map<String, Object> articleData) {
        Article article = new Article();
        String title = (String) articleData.get("title");
        article.setTitle(title);

        String summary = (String) articleData.get("summary");
        article.setSummary(summary);
        // 获取标签列表

        Set<Tag> tags = new HashSet<>();
        Object tagsObject = articleData.get("tags");

        //如果直接强制强化List<String>会有警告，所以需要判断一下
        tags = tagService.initTags(tags, tagsObject);

        article.setTagsSet(tags);


        Long categoryId = ((Integer) articleData.get("categoryId")).longValue();

        Category category = categoryService.findById(categoryId);
        article.setCategory(category);
        //返回的数组

        String content = (String) articleData.get("content");
        article.setContent(content);
        // 处理临时图片
        @SuppressWarnings("unchecked")
        List<Map<String, String>> tempImages = (List<Map<String, String>>) articleData.get("tempImages");
        //[
        //  {
        //    "url": "http://localhost:8090/temp/32113.png"
        //  }
        //]
        if (tempImages != null && !tempImages.isEmpty()) {

            Set<Image> images = null;
            for (Map<String, String> tempImage : tempImages) {
                try {
                    String tempUrl = tempImage.get("url");
                    String tempFileName = tempUrl.substring(tempUrl.lastIndexOf('/') + 1);
                    String newFileName = UUID.randomUUID() +
                            tempFileName.substring(tempFileName.lastIndexOf('.'));
                    // 移动文件
                    logger.info("临时文件路径: " + tempDirectory + tempFileName);
                    Path tempPath = Paths.get(tempDirectory, tempFileName);
                    // 目标文件路径
                    logger.info("目标文件路径: " + uploadDirectory + newFileName);
                    Path targetPath = Paths.get(uploadDirectory, newFileName);
                    Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    // 创建图片实体
                    Image image = new Image();
                    image.setOriginalName(tempFileName);
                    image.setPath(targetPath.toString());
                    image.setUrl(U + "/article_img/" + newFileName);
                    image.setSize(Files.size(targetPath));
                    image.setType(Files.probeContentType(targetPath));
                    image.setArticleId(article.getId());
                    logger.info("httpurl" + image.getUrl());

                    // 更新文章内容中的URL
                    //http://localhost:8090/temp/32113.png
                    //http://localhost:8090/article_img/32113.png
                    content = content.replace(tempUrl, image.getUrl());

                    // 添加到文章的图片集合
                    images = new HashSet<>();
                    images.add(image);

                } catch (IOException e) {
                    throw new RuntimeException("处理图片失败: " + e.getMessage());
                }
            }

            // 更新文章内容
            article.setContent(content);
            //保存图片到数据库
            article.setImages(images);
        }
        return article;
    }

    @Transactional
    @PostMapping
    public Result<?> createArticle(@RequestBody Map<String, Object> articleData) {

        Article saveArticle = articleService.createArticle(setArticle(articleData));
        algoliaService.saveArticle(saveArticle);
        return Result.success(saveArticle);
    }
    //这样会在获取Article实体时，立即初始化tagsSet集合。

    @GetMapping
    public Result<?> getArticle(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize) {


        Page<Article> pageArticles = articleService.findAllArticles(page, pageSize);
        return Result.success(new ArticleResponse(pageArticles.getContent(), pageArticles.getTotalElements()));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteArticle(@PathVariable Long id) {

        if (articleService.deleteArticleById(id)) {
            algoliaService.deleteArticle(id);
            return Result.success("删除成功");
        } else {
            return Result.fail();
        }
    }

    @PutMapping("/{id}")
    public Result<?> updateArticle(@PathVariable Long id, @RequestBody Article article) {

        if (articleService.updateAriticleById(id, article)) {
            algoliaService.deleteArticle(id);
            algoliaService.saveArticle(article);
            return Result.success("更新成功");
        } else {
            return Result.fail();
        }

    }

    //http://localhost:8090/api/articles/category/1
    @GetMapping("/category/{id}")
    public Result<?> getArticleByCategoryId(@PathVariable String id, @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {


        if (id.equalsIgnoreCase("uncategorized")) {
            return getUncategorizedArticles(page, pageSize);
        }

        try {
            Long categoryId = Long.parseLong(id);
            Page<Article> pageArticles = articleService.findArticlesByCategoryId(categoryId, page, pageSize);
            return Result.success(new ArticleResponse(pageArticles.getContent(), pageArticles.getTotalElements()));
        } catch (NumberFormatException e) {
            return Result.fail("无效的分类ID");
        }
    }


    //根据标签获取文章分页
    @GetMapping("/tag/{tagId}")
    public Result<?> getArticlesByTag(@PathVariable Long tagId, @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> articles = articleService.findArticlesByTagId(tagId, page, pageSize);
        return Result.success(articles);


    }


    //未分类的所有文章
    @GetMapping("/category/uncategorized")
    public Result<?> getUncategorizedArticles(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> articles = articleService.findUncategorizedArticles(page, pageSize);
        return Result.success(new ArticleResponse(articles.getContent(), articles.getTotalElements()));
    }

    //获取文章总数
    @GetMapping("/counts")
    public Result<?> countAllArticles() {
        return Result.success(articleService.countAllArticles());
    }



}
