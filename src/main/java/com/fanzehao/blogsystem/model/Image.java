package com.fanzehao.blogsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;  // 原始文件名

    @Column(nullable = false)
    private String path;         // 存储路径

    @Column(nullable = false)
    private String url;          // 访问URL

    private Long size;           // 文件大小

    private String type;         // 文件类型

    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "article_id")
    private Long articleId;      // 文章ID
}