package com.example.springrider.config.scheduler;

import com.example.springrider.domain.search.service.SearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class SearchScheduler {

    private final SearchLogService searchLogService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_RANKING_KEY = "search_rank";

    /**
     * 5분마다 트렌딩 검색어 업데이트 호출
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void updateTrending() {
        searchLogService.updateTrending();
    }

    /**
     * 매 시간마다 오래된 검색 로그 삭제 호출
     */
    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldLogs() {
        searchLogService.deleteOldSearchLogs();
    }

    /**
     * 3분마다 Redis에서 검색 로그를 DB로 flush하고,
     * 인기 검색어 랭킹 초기화 (상위 10개 유지, 점수 1로 초기화)
     */
    @Scheduled(cron = "0 */3 * * * *")
    @Transactional
    public void flushSearchLogsToDB() {
        searchLogService.flushSearchLogsToDB();
        resetSearchRanking();
    }

    /**
     * 인기 검색어 랭킹 초기화: 상위 10개 키워드만 점수 1로 재등록
     */
    private void resetSearchRanking() {
        Set<ZSetOperations.TypedTuple<String>> top10 = redisTemplate.opsForZSet()
                .reverseRangeWithScores(REDIS_RANKING_KEY, 0, 9);

        redisTemplate.delete(REDIS_RANKING_KEY);

        if (top10 != null) {
            for (ZSetOperations.TypedTuple<String> entry : top10) {
                String keyword = entry.getValue();
                if (keyword != null) {
                    redisTemplate.opsForZSet().add(REDIS_RANKING_KEY, keyword, 1.0);
                }
            }
        }
    }
}
