package com.fanzehao.blogsystem.pojo;

import lombok.Data;

@Data
public class CommentDTO {
    private String userId;
    private String articleId;
    private String content;
}
