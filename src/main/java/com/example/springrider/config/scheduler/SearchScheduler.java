package com.example.springrider.config.scheduler;

import com.example.springrider.domain.search.service.SearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SearchScheduler {

    private final SearchLogService searchLogService;

    @Scheduled(cron = "*/1 * * * * *")
    //@Scheduled(cron = "0 0 * * * *")
    public void updateTrending() {
        searchLogService.updateTrending();
    }

    @Scheduled(cron = "0 */10 * * * *")
    //@Scheduled(cron = "0 0 * * * *")
    public void deleteOldLogs() {
        searchLogService.deleteOldSearchLogs();
    }
}

