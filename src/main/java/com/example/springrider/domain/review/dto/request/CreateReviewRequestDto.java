package com.example.springrider.domain.review.dto.request;

import com.example.springrider.domain.review.enums.Rating;
import com.example.springrider.global.validation.EnumValid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateReviewRequestDto {

    @Size(max = 50, message = "리뷰 최대 글자 수는 50자 입니다.")
    private final String contents;

    @NotNull
    @EnumValid(enumClass = Rating.class, message = "올바르지 않은 등급 코드입니다.")
    private final String rating;
}
