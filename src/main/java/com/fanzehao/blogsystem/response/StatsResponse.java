package com.fanzehao.blogsystem.response;

import com.fanzehao.blogsystem.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsResponse {
    private Long totalVisits;
    private Long todayVisits;
    private List<Article> popularArticles;

}
