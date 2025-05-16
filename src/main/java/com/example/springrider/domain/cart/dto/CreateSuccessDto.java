package com.example.springrider.domain.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateSuccessDto {

    private final Long cartItemId;

    private final Long menuId;

    private final Integer quantity;
}
