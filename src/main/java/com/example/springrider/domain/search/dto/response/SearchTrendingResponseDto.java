package com.example.springrider.domain.search.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchTrendingResponseDto {
    private Long id;
    //가게 상호명 또는 메뉴
    private String keyword;
    private Long count;
    public SearchTrendingResponseDto(Long id, String keyword, Long count) {
        this.id = id;
        this.keyword = keyword;
        this.count = count;
    }
}
