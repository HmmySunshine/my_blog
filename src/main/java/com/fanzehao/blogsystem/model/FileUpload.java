package com.fanzehao.blogsystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="file_name", nullable = false)
    private String fileName;

    @Column(name ="file_path", nullable = false)
    private String filePath;

    @Column(name ="file_type", nullable = false)
    private String fileType;


    @Column(name ="file_size", nullable = false)
    private Long fileSize;

    @Column(name ="description")
    private String description;

    @Column(name ="downloads")
    private Integer downloads;

    private Date uploadedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = new Date();
    }


}
