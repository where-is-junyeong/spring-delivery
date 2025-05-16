package com.example.springrider.domain.cart.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindCartItemBulkResponseDto {

    private final List<FindCartItemResponseDto> responseDtos;

    private final Long storeId;

    private final int totalPrice;

    public static FindCartItemBulkResponseDto of(
        List<FindCartItemResponseDto> responseDtos,
        Long storeId, int sumTotalprice
    ) {
        return new FindCartItemBulkResponseDto(
            responseDtos, storeId, sumTotalprice
        );
    }
}
