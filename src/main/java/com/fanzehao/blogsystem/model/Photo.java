package com.fanzehao.blogsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "photos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    private String description;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    private Integer width;
    private Integer height;

    @Column(name = "is_wide")
    private Boolean isWide;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = new Date();
    }

    @Column(name = "http_url")
    private String httpUrl;
}
