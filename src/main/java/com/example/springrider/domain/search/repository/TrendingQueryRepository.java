package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;

import java.util.List;

public interface TrendingQueryRepository {

    List<SearchTrendingResponseDto> trendingKeyword(Long rank);
}