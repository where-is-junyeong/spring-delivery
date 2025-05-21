package com.example.springrider.domain.search.service;

import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCacheService {

    private final StoreRepository storeRepository;

    @Cacheable(
        value = "searchResults",
        key = "#keyword + '_' +  #pageable.pageNumber + '_' + #pageable.pageSize"
    )
    public Page<FindAllStoreResponseDto> getSearchResultsWithCache(String keyword, Pageable pageable){
        return storeRepository.search(keyword, pageable);
    }

}
