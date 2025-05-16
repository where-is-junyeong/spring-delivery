package com.example.springrider.domain.cart.dto.request;

import com.example.springrider.domain.cart.enums.CartItemStatus;
import com.example.springrider.global.validation.EnumValid;
import com.example.springrider.global.validation.NotAllNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NotAllNull
public class UpdateCartItemRequestDto {

    @EnumValid(enumClass = CartItemStatus.class, message = "올바르지 않은 장바구니 상태 코드입니다.")
    private final String status;

    private final Integer quantity;
}
