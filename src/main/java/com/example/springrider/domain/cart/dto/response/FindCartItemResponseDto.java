package com.example.springrider.domain.cart.dto.response;

import com.example.springrider.domain.cart.entity.CartItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindCartItemResponseDto {

    private final Long cartItemId;

    private final String MenuName;

    private final Integer price;

    private final Integer quantity;

    public static FindCartItemResponseDto of(CartItem cartItem) {
        return new FindCartItemResponseDto(
            cartItem.getId(),
            cartItem.getMenu().getName(),
            cartItem.getMenu().getPrice(),
            cartItem.getQuantity()
        );
    }
}
