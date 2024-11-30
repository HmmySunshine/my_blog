package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章仓库接口，继承自 JpaRepository，用于对文章进行 CRUD 操作和自定义查询
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * 根据是否发布状态降序查询文章列表
     * @param isPublished 是否发布
     * @return 文章列表
     */
    List<Article> findByIsPublishedOrderByCreateTimeDesc(Boolean isPublished);
    Page<Article> findAll(Pageable pageable);

    /**
     * 根据标题或内容包含关键词查询文章列表
     * @param title 标题关键词
     * @param content 内容关键词
     * @return 文章列表
     */
    List<Article> findByTitleContainingOrContentContaining(String title, String content);

    @Modifying
    @Query("update Article a set a.views =  :viewCount where a.id = :articleId")
    void incrementViewCount(Long articleId, Integer viewCount);
}
