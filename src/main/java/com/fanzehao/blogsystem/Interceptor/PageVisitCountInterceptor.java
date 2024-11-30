package com.fanzehao.blogsystem.Interceptor;

import com.fanzehao.blogsystem.Timer.TempFileCleanupTask;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PageVisitCountInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    //总访问量
    private static final String TOTAL_VISIT_COUNT = "total_visit_count";
    //今日访问量
    private static final String TODAY_VISIT_COUNT = "today_visit_count";
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TempFileCleanupTask.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        redisTemplate.opsForValue().increment(TOTAL_VISIT_COUNT,1);
        redisTemplate.opsForValue().increment(TODAY_VISIT_COUNT,1);
        logger.info("总访问量：" + redisTemplate.opsForValue().get(TOTAL_VISIT_COUNT));
        logger.info("今日访问量：" + redisTemplate.opsForValue().get(TODAY_VISIT_COUNT));

        return true;
    }
}
