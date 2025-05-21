package com.example.springrider.config.scheduler;

import com.example.springrider.domain.search.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchScheduler {

    private final TrendService trendService;

    // 검색어 백업
    @Scheduled(cron = "0 0 * * * *")
    public void saveTrend(){
        trendService.saveTrend();
    }

}
