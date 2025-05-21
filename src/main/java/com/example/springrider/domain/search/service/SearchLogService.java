package com.example.springrider.domain.search.service;

import com.example.springrider.config.redis.PageResponse;
import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.SearchLog;
import com.example.springrider.domain.search.entity.Trending;
import com.example.springrider.domain.search.repository.SearchLogRepository;
import com.example.springrider.domain.search.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;
    private final TrendingRepository trendingRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_RANKING_KEY = "search_rank";
    /**
     * 즉시 DB에 검색 로그 저장 (트랜잭션 별도 분리)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(String keyword) {
        SearchLog searchLog = new SearchLog(keyword);
        searchLogRepository.save(searchLog);
    }
    /**
     * Redis에 검색 로그 적재 (배치용)
     * ZSet에 점수 누적도 함께 처리
     */
    public void enqueue(String keyword) {
        redisTemplate.opsForList().leftPush("search_logs", keyword);
        redisTemplate.opsForZSet().incrementScore(REDIS_RANKING_KEY, keyword, 1);
    }

    /**
     * 최근 1시간 기준 인기 검색어 (DB 기반)
     */
    public Page<SearchTrendingResponseDto> findV1(Pageable pageable) {

        return trendingRepository.findTrending(pageable);
    }

    /**
     * 실시간 인기 검색어 조회 (Redis 기반, 캐시 적용)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "searchCacheLog", key = "'trending:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public PageResponse<SearchTrendingResponseDto> findV2(Pageable pageable) {

        Set<ZSetOperations.TypedTuple<String>> topKeywords = redisTemplate.opsForZSet()
                .reverseRangeWithScores(REDIS_RANKING_KEY, 0, 9);

        List<SearchTrendingResponseDto> dtoList = new ArrayList<>();
        Long rank = 0L;

        if (topKeywords != null) {
            for (ZSetOperations.TypedTuple<String> tuple : topKeywords) {
                rank++;
                dtoList.add(new SearchTrendingResponseDto(rank, tuple.getValue(), tuple.getScore().longValue()));
            }
        }

        // 수동 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());
        List<SearchTrendingResponseDto> pageContent = dtoList.subList(start, end);

        return PageResponse.from(new PageImpl<>(pageContent, pageable, dtoList.size()));
    }

    /**
     * 1시간 전까지 검색 로그 기반으로 트렌딩 데이터 갱신
     */
    @Transactional
    public void updateTrending() {
        LocalDateTime anHourAgo = LocalDateTime.now().minusHours(1);
        Page<SearchTrendingResponseDto> trendingKeywords =
                searchLogRepository.findTrending(anHourAgo, PageRequest.of(0, 10));

        trendingRepository.deleteAllInBatch();

        List<Trending> trendingList = trendingKeywords.stream()
                .map(dto -> new Trending(dto.getKeyword(), dto.getCount()))
                .toList();

        trendingRepository.saveAll(trendingList);
    }

    /**
     * 1주일 이상 된 오래된 검색 로그 삭제
     */
    @Transactional
    public void deleteOldSearchLogs() {
        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);
        searchLogRepository.deleteSearchLogByCreatedAtBefore(aWeekAgo);
    }


    /**
     * Redis에서 검색 로그 모두 꺼내 DB에 배치 저장
     */
    @Transactional
    public void flushSearchLogsToDB() {
        List<SearchLog> logs = new ArrayList<>();

        while (Boolean.TRUE.equals(redisTemplate.hasKey("search_logs"))) {
            String keyword = redisTemplate.opsForList().rightPop("search_logs");
            if (keyword != null) {
                logs.add(new SearchLog(keyword));
            }
        }
        if (!logs.isEmpty()) {
            searchLogRepository.saveAll(logs);
        }
    }

}
