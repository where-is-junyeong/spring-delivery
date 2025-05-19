package com.example.springrider.domain.search.service;
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

    // 검색 API - v1 (캐시 미적용)
    public Page<SearchResponseDto> findv1(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);
        // 중복 코드일 가능성 농후
        storePage.forEach(store -> {
            // 상점 이름이 keyword 포함된 경우만 저장
            if (store.getName().contains(keyword)){
                searchLogService.create(store.getName());
            }

            // 메뉴 중 keyword 포함된 것만 필터링 후 로깅
            store.getMenus().stream()
                    .map(Menu::getName)
                    .filter(menuName -> menuName.contains(keyword))
                    .forEach(searchLogService::create);
        });

        return storePage.map(SearchResponseDto::of);
    }

    // 검색 API - v2 (캐시 적용)
    @Cacheable(cacheNames = "searchCache")
    public Page<SearchResponseDto> findv2(String keyword, Pageable pageable) {
        Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);
        // 중복 코드일 가능성 농후
        storePage.forEach(store -> {
            // 상점 이름이 keyword 포함된 경우만 저장
            if (store.getName().contains(keyword)){
                searchLogService.create(store.getName());
            }

            // 메뉴 중 keyword 포함된 것만 필터링 후 로깅
            store.getMenus().stream()
                    .map(Menu::getName)
                    .filter(menuName -> menuName.contains(keyword))
                    .forEach(searchLogService::create);
        });

        return storePage.map(SearchResponseDto::of);
    }

}
