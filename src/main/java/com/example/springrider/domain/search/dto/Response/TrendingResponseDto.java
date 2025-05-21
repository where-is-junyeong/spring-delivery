package com.example.springrider.domain.search.dto.Response;

import com.example.springrider.domain.search.entity.Keyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TrendingResponseDto {

    private final String keyword;
    private final Long count;
    private final LocalDateTime createdAt;

    public static TrendingResponseDto from(Keyword keyword){
        return new TrendingResponseDto(
                keyword.getKeyword(),
                keyword.getCount(),
                keyword.getCreatedAt()
        );
    }
}
