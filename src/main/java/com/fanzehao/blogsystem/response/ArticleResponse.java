package com.fanzehao.blogsystem.response;
import com.fanzehao.blogsystem.model.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleResponse {
    private List<Article> articles;
    private long total;

    public ArticleResponse(List<Article> articles, long total) {
        this.articles = articles;
        this.total = total;
    }

    // getters and setters
}