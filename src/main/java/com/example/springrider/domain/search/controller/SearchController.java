package com.example.springrider.domain.search.controller;

import com.example.springrider.config.redis.PageResponse;
import com.example.springrider.domain.search.dto.response.SearchResponseDto;
import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.service.SearchLogService;
import com.example.springrider.domain.search.service.SearchService;
import com.example.springrider.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SearchLogService searchLogService;

    // 검색 API - v1 (캐시 미적용)
    @GetMapping("api/search/v1")
    public ApiResponse<Page<SearchResponseDto>> searchV1(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.ok(searchService.findv1(keyword, pageable));
    }

    // 검색 API - v2 (캐시 적용)
    @GetMapping("api/search/v2")
    public ApiResponse<PageResponse<SearchResponseDto>> searchV2(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.ok(searchService.findv2(keyword, pageable));
    }

    // 인기 검색어 API - v1 (캐시 미적용) - 시간에 따른 데이터
    @GetMapping("api/search/trending/v1")
    public ApiResponse<Page<SearchTrendingResponseDto>> trendingV1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.ok(searchLogService.findV1(pageable));
    }

    // 인기 검색어 API - v2 (캐시 적용) - 실시간
    @GetMapping("api/search/trending/v2")
    public ApiResponse<PageResponse<SearchTrendingResponseDto>> trendingV2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.ok(searchLogService.findV2(pageable));
    }
}
