package com.example.springrider.domain.order.dto.response;

import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.store.entity.Store;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OwnerOrderResponseDto {

    private final Long storeId;
    private final String storeName;
    private final List<OrderResponseDto> orders;

    public static OwnerOrderResponseDto of(Store store, List<Order> orders) {
        return new OwnerOrderResponseDto(
            store.getId(),
            store.getName(),
            orders.stream()
                .map(OrderResponseDto::ofOwner)
                .toList()
        );
    }
}