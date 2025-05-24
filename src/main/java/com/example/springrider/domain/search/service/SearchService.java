package com.example.springrider.domain.search.service;

import com.example.springrider.config.redis.PageResponse;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.search.dto.response.SearchResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchLogService searchLogService;
    private final RedisSearchUtil redisSearchUtil;
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

        long start = (long) pageable.getPageNumber() * pageable.getPageSize();
        long end = start + pageable.getPageSize() - 1;

        List<Long> storeIds =redisSearchUtil.getStoreIds(keyword,start,end);

        // Redis 캐시 미스 시 로직 위임
        if (storeIds.isEmpty()) {
            return PageResponse.from(handleCacheMiss(keyword, pageable));
        }

        // Redis 캐시 히트 시 ID → DB 조회 → DTO 정렬
        List<Store> stores = storeRepository.findByIdIn(storeIds);
        Map<Long, Store> storeMap = stores.stream().collect(Collectors.toMap(Store::getId, Function.identity()));

        List<SearchResponseDto> sortedDtos = storeIds.stream()
                .map(storeMap::get)
                .filter(Objects::nonNull)
                .map(SearchResponseDto::of)
                .toList();

        long totalElements = redisSearchUtil.getTotal(keyword);
        return PageResponse.from(new PageImpl<>(sortedDtos, pageable, totalElements));
    }

    private Page<SearchResponseDto> handleCacheMiss(String keyword, Pageable pageable) {

        Page<Store> storePage = storeRepository.searchByKeywordPaged(keyword, pageable);
        List<Store> allStores = storeRepository.searchByKeywordWithLimit(keyword, pageable.getOffset(), (long) pageable.getPageSize()); // 전체 ID 확보용

        if (!allStores.isEmpty()) {
            redisSearchUtil.updateIndex(keyword,allStores);
        }

        boolean matches = storePage.stream().anyMatch(store ->
                store.getName().contains(keyword) ||
                        store.getMenus().stream().anyMatch(menu -> menu.getName().contains(keyword)));

        if (matches) {
            searchLogService.enqueue(keyword);
        }

        return storePage.map(SearchResponseDto::of);
    }

}
