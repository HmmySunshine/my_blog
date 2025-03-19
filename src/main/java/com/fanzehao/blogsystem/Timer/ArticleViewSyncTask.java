package com.fanzehao.blogsystem.Timer;

import com.fanzehao.blogsystem.Service.ArticleService;
import com.fanzehao.blogsystem.model.VisitCount;
import com.fanzehao.blogsystem.repository.VisitCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Component
public class ArticleViewSyncTask {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private VisitCountRepository visitCountRepository;

    @Autowired
    private ArticleService articleService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ArticleViewSyncTask.class);
    private static final String ARTICLE_VIEW_COUNT_KEY = "article:view:count";

    //实际每小时同步一次

    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
    //为了测试所以一分钟执行一次
    public void syncViewCounts() {
        Map<Object, Object> viewCounts = redisTemplate.opsForHash().entries(ARTICLE_VIEW_COUNT_KEY);

        if (viewCounts.isEmpty()) {
            logger.info("Redis中没有文章对应的阅读量记录");
            return;
        }

        for (Map.Entry<Object, Object> entry : viewCounts.entrySet()) {
            Long articleId = Long.valueOf(entry.getKey().toString());
            Integer viewCount = Integer.valueOf(entry.getValue().toString());
            logger.info("文章ID: {}, 阅读量: {}", articleId, viewCount);

            articleService.updateViewCount(articleId, viewCount);
            // 删除已同步的记录

        }
    }
}
