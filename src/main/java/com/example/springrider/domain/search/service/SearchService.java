package com.example.springrider.domain.search.service;

import com.example.springrider.config.redis.PageResponse;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.search.dto.response.SearchResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchLogService searchLogService;
    /**
     * 검색 API v1 - DB 조회, 캐시 미적용
     * 검색어가 상점명 또는 메뉴명에 포함된 경우 로그 저장 (즉시 DB 저장)
     */
    public Page<SearchResponseDto> findv1(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);

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
    @Cacheable(cacheNames = "searchCache", key = "#keyword + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public PageResponse<SearchResponseDto> findv2(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);

        boolean matchFound = storePage.stream().anyMatch(store ->
                store.getName().contains(keyword) ||
                        store.getMenus().stream().anyMatch(menu -> menu.getName().contains(keyword))
        );

        if (matchFound) {
            // Redis List에 로그 저장, DB 저장은 배치로 처리
            searchLogService.enqueue(keyword);
        }

        return PageResponse.from(storePage.map(SearchResponseDto::of));
    }
}
