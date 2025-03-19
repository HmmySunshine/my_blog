package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.StatsService;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.response.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public StatsResponse stats(){
        return statsService.getStats();
    }

    @GetMapping("/totalVisitCount")
    public Result<?> totalVisitCount(){
        return Result.success(statsService.getTotalVisitCount());
    }
}
