package com.example.springrider.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCartItemRequestDto {


    @NotNull(message = "메뉴Id는 필수 값입니다.")
    private final Long menuId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private final Integer quantity;
}