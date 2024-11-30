package com.fanzehao.blogsystem.model;

import com.fanzehao.blogsystem.pojo.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updateAt;
}
