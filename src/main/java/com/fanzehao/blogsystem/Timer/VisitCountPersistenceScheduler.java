package com.fanzehao.blogsystem.Timer;

import com.fanzehao.blogsystem.model.VisitCount;
import com.fanzehao.blogsystem.repository.VisitCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class VisitCountPersistenceScheduler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private VisitCountRepository visitCountRepository; // 假设你有一个用来保存访问量的数据库 repository

    private static final String TOTAL_VISIT_COUNT = "total_visit_count";

    // 每天晚上 23:59 执行，存储总访问量到数据库

    //@Scheduled(cron = "0 59 23 * * ?") // 每天 23:59 执行
    //为了测试一分钟执行一次
    @Transactional
    //@Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void storeTotalVisitCount() {
        String totalVisitCount = redisTemplate.opsForValue().get(TOTAL_VISIT_COUNT);
        if (totalVisitCount != null) {
            int updatedRows = visitCountRepository.updateTotalVisitCount(Long.parseLong(totalVisitCount), 1L);
            if (updatedRows == 0) {
                // 如果没有更新任何行,则创建一个新的记录
                VisitCount newVisitCount = new VisitCount();
                newVisitCount.setId(1L);
                newVisitCount.setTotalVisitCount(Long.parseLong(totalVisitCount));
                visitCountRepository.save(newVisitCount);
            }
            System.out.println("总访问量已保存到数据库: " + totalVisitCount);
        }
    }
}