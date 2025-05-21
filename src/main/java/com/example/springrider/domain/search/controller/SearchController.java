package com.example.springrider.domain.search.controller;

import com.example.springrider.domain.search.dto.Response.SearchResultResponseDto;
import com.example.springrider.domain.search.dto.Response.TrendingResponseDto;
import com.example.springrider.domain.search.service.SearchService;
import com.example.springrider.domain.search.service.TrendService;
import com.example.springrider.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    private final TrendService trendService;

    //검색 v1
    @GetMapping
    public ApiResponse<Page<SearchResultResponseDto>>search (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword
    ){
        Page<SearchResultResponseDto> response = searchService.search(keyword,page,size);
        return ApiResponse.ok(response);
    }

    //인기 검색어 - v1
    @GetMapping("/trend")
    public ApiResponse<List<TrendingResponseDto>> trend(){

        List<TrendingResponseDto> response = trendService.trend();
        return ApiResponse.ok(response);
    }

    //검색 v2
    @GetMapping("/v2")
    public ApiResponse<Page<SearchResultResponseDto>>searchV2(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword
    ){

        Page<SearchResultResponseDto> response = searchService.searchV2(keyword,page,size);
        return ApiResponse.ok(response);
    }

    //인기 검색어 - v2
    @GetMapping("/v2/trend")
    public ApiResponse<List<TrendingResponseDto>> trendV2(){

        List<TrendingResponseDto> response = trendService.trendV2();
        return ApiResponse.ok(response);
    }
}
