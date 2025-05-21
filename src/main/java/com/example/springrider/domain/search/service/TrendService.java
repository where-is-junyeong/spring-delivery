package com.example.springrider.domain.search.service;

import com.example.springrider.domain.search.dto.Response.SearchResultResponseDto;
import com.example.springrider.domain.search.dto.Response.TrendingResponseDto;
import com.example.springrider.domain.search.entity.Keyword;
import com.example.springrider.domain.search.repository.SearchRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrendService {

    private final StoreRepository storeRepository;
    private final SearchRepository searchRepository;
    private final CacheManager cacheManager;


    // 검색 결과 호출 v2
    @Cacheable(value = "searchKeyword", key = "#keyword", unless = "#result.isEmpty()")
    public List<SearchResultResponseDto> getCachedSearchResults(String keyword) {

        List<Store> stores = storeRepository.SearchByKeyword(keyword);
        return stores.stream()
                .map(SearchResultResponseDto::from)
                .toList();
    }

    //검색 횟수 증가 v1
    @Transactional
    public void increaseKeywordCount(String keyword) {
        Optional<Keyword> savedKeyword = searchRepository.findByKeyword(keyword);
        if (savedKeyword.isPresent()) {
            savedKeyword.get().increaseCount();
        } else {
            searchRepository.save(new Keyword(keyword));
        }
    }

    //검색 횟수 증가 v2 (캐시 사용)
    @CachePut(value = "keywordCount", key = "#keyword")
    public Long increaseKeywordCountV2(String keyword) {
        Cache cache = cacheManager.getCache("keywordCount");
        if (cache == null) return 1L;

        Long count = cache.get(keyword, Long.class);
        if (count == null) count = 0L;

        return count + 1;
    }

    //인기 검색어 조회 v1
    public List<TrendingResponseDto> trend(){

        LocalDateTime time = LocalDateTime.now().minusHours(1);
        List<Keyword> keyword = searchRepository.findTrendByOrderByCount(time);
        return keyword.stream()
                .map(TrendingResponseDto::from)
                .collect(Collectors.toList());
    }

    //인기 검색어 조회 v2
    public List<TrendingResponseDto> trendV2(){

        LocalDateTime time = LocalDateTime.now().minusHours(1);
        List<Keyword> trend = searchRepository.findTrendByOrderByCount(time);
        return trend.stream().map(TrendingResponseDto::from).toList();

    }

    // 캐시 백업
    @Transactional
    public void saveTrend(){

        Cache cache = cacheManager.getCache("keywordCount");
        if (!(cache instanceof CaffeineCache caffeineCache)) return;

        Map<Object, Object> cacheMap = caffeineCache.getNativeCache().asMap();

        for (Map.Entry<Object, Object> entry : cacheMap.entrySet()) {
            if (!(entry.getKey() instanceof String keyword)) continue;
            if (!(entry.getValue() instanceof Long count)) continue;

            // 새로운 Keyword 객체 저장
            searchRepository.save(new Keyword(keyword, count));
        }
        // 캐시 초기화
        caffeineCache.clear();
    }
}
