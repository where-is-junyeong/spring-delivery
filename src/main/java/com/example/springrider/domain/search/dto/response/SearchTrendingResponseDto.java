package com.example.springrider.domain.search.dto.response;

import lombok.Getter;

@Getter
public class SearchTrendingResponseDto {
    private final Long id;
    //가게 상호명 또는 메뉴
    private final String keyword;
    private final Long count;
    public SearchTrendingResponseDto(Long id, String keyword, Long count) {
        this.id = id;
        this.keyword = keyword;
        this.count = count;
    }
}
