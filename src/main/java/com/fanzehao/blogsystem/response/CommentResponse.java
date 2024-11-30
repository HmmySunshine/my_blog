package com.fanzehao.blogsystem.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private String content;
    private Date createdAt;
    private String username;

    private Long id;
}
