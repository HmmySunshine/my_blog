package com.fanzehao.blogsystem.Timer;

import com.fanzehao.blogsystem.model.VisitCount;
import com.fanzehao.blogsystem.repository.VisitCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class VisitCountPersistenceScheduler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private VisitCountRepository visitCountRepository; // 用来保存访问量的数据库 repository

    private static final String TOTAL_VISIT_COUNT = "total_visit_count";
    private static final String TODAY_VISIT_COUNT = "today_visit_count";

    // 每天晚上 23:59 执行，存储总访问量和今日访问量到数据库
    @Transactional
    @Scheduled(cron = "0 0 23 * * ?")  // 每天 23:59 执行
   // @Scheduled(cron = "0 * * * * ?") // 测试每分钟执行一次
    public void storeVisitCounts() {
        // 获取 Redis 中存储的总访问量和今日访问量
        String totalVisitCountStr = redisTemplate.opsForValue().get(TOTAL_VISIT_COUNT);
        String todayVisitCountStr = redisTemplate.opsForValue().get(TODAY_VISIT_COUNT);

        if (totalVisitCountStr != null) {
            long totalVisitCount = Long.parseLong(totalVisitCountStr);
            // 存储总访问量到数据库
            int updatedRows = visitCountRepository.updateTotalVisitCount(totalVisitCount, 1L);
            if (updatedRows == 0) {
                // 如果没有更新任何行,则创建一个新的记录
                VisitCount newVisitCount = new VisitCount();
                newVisitCount.setId(1L);
                newVisitCount.setTotalVisitCount(totalVisitCount);
                newVisitCount.setTodayVisitCount(0L);  // 今日访问量清零
                visitCountRepository.save(newVisitCount);
            }
            System.out.println("总访问量已保存到数据库: " + totalVisitCount);
        }

        if (todayVisitCountStr != null) {
            long todayVisitCount = Long.parseLong(todayVisitCountStr);
            // 存储今日访问量到数据库
            int updatedRows = visitCountRepository.updateTodayVisitCount(todayVisitCount, 1L);
            if (updatedRows == 0) {
                // 如果没有更新任何行，则创建新的记录
                VisitCount newVisitCount = new VisitCount();
                newVisitCount.setId(1L);
                newVisitCount.setTodayVisitCount(todayVisitCount);
                visitCountRepository.save(newVisitCount);
            }
            System.out.println("今日访问量已保存到数据库: " + todayVisitCount);
        }

        // 清空 Redis 中的今日访问量
        redisTemplate.opsForValue().set(TODAY_VISIT_COUNT, "0");
    }
}
