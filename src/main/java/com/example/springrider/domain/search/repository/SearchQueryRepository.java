package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.TrendingKeywordResponseDto;
import java.util.List;

public interface SearchQueryRepository {

    List<TrendingKeywordResponseDto> trendingKeyword(Long rank);
}
