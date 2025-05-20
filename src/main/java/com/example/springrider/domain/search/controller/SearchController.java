package com.example.springrider.domain.search.controller;

import com.example.springrider.domain.search.dto.response.TrendingKeywordResponseDto;
import com.example.springrider.domain.search.service.SearchService;
import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.global.response.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/v1")
    public ApiResponse<Map<String, Page<FindAllStoreResponseDto>>> searchV1(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<FindAllStoreResponseDto> stores = searchService.searchV1(keyword, initPageable(page, size));
        Map<String, Page<FindAllStoreResponseDto>> response = new HashMap<>();
        response.put("stores", stores);

        return ApiResponse.ok(response);
    }

    @GetMapping("/v1/trending")
    public ApiResponse<List<TrendingKeywordResponseDto>> trendingV1(
        @RequestParam(defaultValue = "10") Long rank
    ) {
        return ApiResponse.ok(searchService.trendingV1(rank));
    }

    @GetMapping("/v2")
    public ApiResponse<Map<String, Page<FindAllStoreResponseDto>>> searchV2(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<FindAllStoreResponseDto> stores = searchService.searchV2(keyword, initPageable(page, size));
        Map<String, Page<FindAllStoreResponseDto>> response = new HashMap<>();
        response.put("stores", stores);

        return ApiResponse.ok(response);
    }

    @GetMapping("/v2/trending")
    public ApiResponse<List<TrendingKeywordResponseDto>> trendingV2(
        @RequestParam(defaultValue = "10") Long rank
    ) {
        return ApiResponse.ok(searchService.trendingV2(rank));
    }

    private Pageable initPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

}
