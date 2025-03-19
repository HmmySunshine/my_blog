package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.VisitCount;
import com.fanzehao.blogsystem.repository.ArticleRepository;
import com.fanzehao.blogsystem.repository.VisitCountRepository;
import com.fanzehao.blogsystem.response.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatsService {

    @Autowired
    private VisitCountRepository visitCountRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public StatsResponse getStats() {
        VisitCount visitCount = visitCountRepository.getById(1L);
        List<Article> top10ByOrderByViewsDesc = articleRepository.findTop10ByOrderByViewsDesc();
        StatsResponse statsResponse = new StatsResponse(visitCount.getTotalVisitCount(), visitCount.getTodayVisitCount(), top10ByOrderByViewsDesc);
        return statsResponse;
    }
    public Long getTotalVisitCount() {
        return visitCountRepository.getTotalVisitCount(1L);
    }
}
