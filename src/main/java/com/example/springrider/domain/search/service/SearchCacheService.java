package com.example.springrider.domain.search.service;

import com.example.springrider.domain.search.dto.response.TrendingKeywordResponseDto;
import com.example.springrider.domain.search.repository.SearchRepository;
import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchCacheService {

    private final StoreRepository storeRepository;
    private final SearchRepository searchRepository;

    @Transactional(readOnly = true)
    @Cacheable(
        value = "searchResults",
        key = "#keyword + '_' +  #pageable.pageNumber + '_' + #pageable.pageSize"
    )
    public Page<FindAllStoreResponseDto> getSearchResultsWithCache(String keyword, Pageable pageable){
        return storeRepository.search(keyword, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "trendingKeywords", key = "#rank")
    public List<TrendingKeywordResponseDto> getTrendingKeywordsWithCache(Long rank){
        return searchRepository.trendingKeyword(rank);
    }

}
