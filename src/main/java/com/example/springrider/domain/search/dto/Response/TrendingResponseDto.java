package com.example.springrider.domain.search.dto.Response;

import com.example.springrider.domain.search.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TrendingResponseDto {

    private final String keyword;
    private final Long count;

    public static TrendingResponseDto of(String keyword, Long count) {
        return new TrendingResponseDto(keyword, count);
    }

    public static TrendingResponseDto from(Keyword keyword){
        return new TrendingResponseDto(
                keyword.getKeyword(),
                keyword.getCount()
        );
    }
}
