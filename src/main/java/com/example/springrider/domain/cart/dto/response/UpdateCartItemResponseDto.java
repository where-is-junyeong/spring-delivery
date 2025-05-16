package com.example.springrider.domain.cart.dto.response;

import com.example.springrider.domain.cart.entity.CartItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCartItemResponseDto {

    private final String Status;

    private final Integer quantity;

    public static UpdateCartItemResponseDto of(CartItem cartItem) {
        return new UpdateCartItemResponseDto(
            cartItem.getStatus().toString(),
            cartItem.getQuantity()
        );
    }
}
