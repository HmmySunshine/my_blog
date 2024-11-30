package com.fanzehao.blogsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "articles")
public class Article {
    // 标识该字段为实体类的主键
    @Id
    // 自动生成主键的值
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 定义实体类的主键类型为Long
    private Long id;

    // 表示该字段不能为空
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    /*cascade = CascadeType.ALL：这个属性定义了级联操作。
  CascadeType.ALL表示所有的实体状态变化（包括新增、更新、删除和刷新）都会被级联到关联的实体。也就是说，如果你删除一个Article实体，所有关联的Image实体也会被删除。

orphanRemoval = true：这个属性定义了孤儿删除策略。
当设置为true时，如果一个Image实体不再与任何Article实体关联，那么这个Image实体将被自动删除。这可以用于自动清理不再使用的关联实体。/

   */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Set<Image> images = new HashSet<>();



    @ManyToMany(fetch = FetchType.EAGER)
// 表示该注解用于定义多对多关系，fetch属性表示加载方式，EAGER表示立即加载
    @JoinTable(name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tagsSet = new HashSet<>();

    @CreationTimestamp
    // 注：@CreationTimestamp注解用于自动填充实体类中的createTime字段，表示创建时间
    private LocalDateTime createTime;
    @UpdateTimestamp
    // 注：@UpdateTimestamp注解用于自动填充实体类中的updateTime字段，表示更新时间
    private LocalDateTime updateTime;
    private Integer views = 0;
    private Boolean isPublished = true;


    @Column(length = 500)
    private String summary;
}
