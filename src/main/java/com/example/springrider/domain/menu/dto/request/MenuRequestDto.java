package com.example.springrider.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuRequestDto {

    @NotBlank(message = "음식 이름은 필수입니다.")
    private final String name;

    @NotNull(message = "음식 가격은 필수입니다.")
    private final Integer price;

    @NotBlank(message = "음식 설명은 필수입니다.")
    private final String contents;

    @NotBlank(message = "음식 장르는 필수입니다.")
    private final String category;

}
