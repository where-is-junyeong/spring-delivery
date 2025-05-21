package com.example.springrider.domain.search.service;

import com.example.springrider.domain.search.dto.Response.SearchResultResponseDto;
import com.example.springrider.domain.search.repository.SearchRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final StoreRepository storeRepository;
    private final TrendService trendService;

    // 검색 v1
    @Transactional
    public Page<SearchResultResponseDto> search(String keyword, int page, int size){
        Pageable pageable= PageRequest.of(page - 1 ,size);
        Page<Store> storePage = storeRepository.SearchPageByKeyword(keyword, pageable);

        //검색어 count + 1
        trendService.increaseKeywordCount(keyword);

        return storePage.map(SearchResultResponseDto::from);
    }

    //검색 v2
    public Page<SearchResultResponseDto> searchV2(String keyword, int page, int size){
        //검색 횟수 증가
        trendService.increaseKeywordCountV2(keyword);
        //검색 결과 호출 list -> page
        List<SearchResultResponseDto> resultList = trendService.getCachedSearchResults(keyword);
        int start = (page - 1) * size;
        int end = Math.min(start + size, resultList.size());
        List<SearchResultResponseDto> pageContent =
                start >= resultList.size() ? Collections.emptyList() : resultList.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page - 1, size), resultList.size());

    }



}
