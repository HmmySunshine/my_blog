package com.fanzehao.blogsystem.model;

import lombok.Data;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlgoliaRecord {
    private String objectID;  // 必须的，用作唯一标识
    private String title;
    private String content;
    private String summary;
    private String categoryName;
    private List<String> tags;
    private String url;      // 文章链接
    private LocalDateTime createTime;

    public static AlgoliaRecord fromArticle(Article article) {
        AlgoliaRecord record = new AlgoliaRecord();
        record.setObjectID(article.getId().toString());
        record.setTitle(article.getTitle());
        record.setContent(Jsoup.parse(article.getContent()).text()); // 清除HTML标签
        record.setSummary(article.getSummary());
        record.setCategoryName(article.getCategory().getName());
        record.setTags(article.getTagsSet().stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        record.setUrl("/article/" + article.getId());  // 前端路由路径
        record.setCreateTime(article.getCreateTime());
        return record;
    }
}