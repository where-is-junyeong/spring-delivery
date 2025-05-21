package com.example.springrider.domain.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrendingKeywordResponseDto {

    private Long count;
    private String keyword;
}
