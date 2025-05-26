package com.example.springrider.domain.search.service;

import com.example.springrider.config.redis.PageResponse;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.search.dto.response.SearchResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchLogService searchLogService;
    private final SearchLogBatchService searchLogBatchService;
    private final RedisTemplate<String, String> redisTemplate;
    /**
     * 검색 API v1 - DB 조회, 캐시 미적용
     * 검색어가 상점명 또는 메뉴명에 포함된 경우 로그 저장 (즉시 DB 저장)
     */
    public Page<SearchResponseDto> findv1(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.searchByKeywordPaged(keyword, pageable);

        storePage.forEach(store -> {
            // 상점 이름에 키워드 포함 시 즉시 로그 저장
            if (store.getName().contains(keyword)) {
                searchLogService.create(store.getName());
            }

            // 메뉴 중 키워드 포함된 경우 즉시 로그 저장
            store.getMenus().stream()
                    .map(Menu::getName)
                    .filter(menuName -> menuName.contains(keyword))
                    .forEach(searchLogService::create);
        });

        return storePage.map(SearchResponseDto::of);
    }
    /**
     * 검색 API v2 - 캐시 적용
     * Redis에 검색어 로그를 쌓고 DB 저장은 배치 처리로 위임
     */
    @Cacheable(value = "searchCache", key = "#keyword + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public PageResponse<SearchResponseDto> findv2(String keyword, Pageable pageable) {

        String redisKey = "search::" + keyword;
        long start = (long) pageable.getPageNumber() * pageable.getPageSize();
        long end = start + pageable.getPageSize() - 1;

        Set<String> storeIdStrings = getStoreIdsFromRedis(redisKey, start, end);

        // Redis 캐시 미스 시 로직 위임
        if (storeIdStrings == null || storeIdStrings.isEmpty()) {
            return PageResponse.from(handleCacheMiss(keyword, pageable));
        }
        // Redis 캐시 히트 시 ID → DB 조회 → DTO 정렬
        List<Long> storeIds = storeIdStrings.stream().map(Long::valueOf).toList();
        List<Store> stores = storeRepository.findByIdIn(storeIds);
        Map<Long, Store> storeMap = stores.stream().collect(Collectors.toMap(Store::getId, Function.identity()));

        List<SearchResponseDto> sortedDtos = storeIds.stream()
                .map(storeMap::get)
                .filter(Objects::nonNull)
                .map(SearchResponseDto::of)
                .toList();

        long totalElements = Optional.ofNullable(redisTemplate.opsForZSet().zCard(redisKey)).orElse((long) sortedDtos.size());

        return PageResponse.from(new PageImpl<>(sortedDtos, pageable, totalElements));
    }

    /**
     * 검색 API v1 - DB 조회, 캐시 미적용, INSERT IGNORE 와 복합키를 통한 배치 사용
     * 검색어가 상점명 또는 메뉴명에 포함된 경우 로그 저장 (즉시 DB 저장)
     */
    public Page<SearchResponseDto> findV1Batch(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.searchByKeywordPaged(keyword, pageable);

        storePage.forEach(store -> {
            // 상점 이름에 키워드 포함 시 즉시 로그 저장
            if (store.getName().contains(keyword)) {
                searchLogBatchService.create(store.getName());
            }

            // 메뉴 중 키워드 포함된 경우 즉시 로그 저장
            store.getMenus().stream()
                    .map(Menu::getName)
                    .filter(menuName -> menuName.contains(keyword))
                    .forEach(searchLogBatchService::create);
        });

        return storePage.map(SearchResponseDto::of);
    }

    private Set<String> getStoreIdsFromRedis(String redisKey, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(redisKey, start, end);
    }

    // no hit redis or redis's cache
    private Page<SearchResponseDto> handleCacheMiss(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.searchByKeywordPaged(keyword, pageable);

        List<Store> allStores = storeRepository.searchByKeywordAll(keyword,pageable); // 전체 ID 확보용
        if (!allStores.isEmpty()) {
            cacheStoreIds(keyword, allStores);
        }

        boolean matches = storePage.stream().anyMatch(store ->
                store.getName().contains(keyword) ||
                        store.getMenus().stream().anyMatch(menu -> menu.getName().contains(keyword)));

        if (matches) {
            searchLogService.enqueue(keyword);
        }

        return storePage.map(SearchResponseDto::of);
    }

    //redis에 정보 찾기
    private void cacheStoreIds(String keyword, List<Store> stores) {
        String redisKey = "search::" + keyword;
        Set<TypedTuple<String>> zSetEntries = stores.stream()
                .map(store -> new DefaultTypedTuple<>(
                        store.getId().toString(),
                        (double) System.currentTimeMillis()
                ))
                .collect(Collectors.toSet());

        redisTemplate.opsForZSet().add(redisKey, zSetEntries);
        redisTemplate.expire(redisKey, Duration.ofHours(1));
    }

}
