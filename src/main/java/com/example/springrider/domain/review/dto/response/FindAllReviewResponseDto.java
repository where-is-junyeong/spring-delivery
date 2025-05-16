package com.example.springrider.domain.review.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindAllReviewResponseDto {

    private final String storeName;

    private final List<ReviewResponseDto> reviews;

    public static FindAllReviewResponseDto of(String storeName, List<ReviewResponseDto> reviews) {
        return new FindAllReviewResponseDto(storeName, reviews);
    }
}
