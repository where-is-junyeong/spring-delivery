package com.example.springrider.domain.store.dto.response;

import com.example.springrider.domain.store.entity.Store;
import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindAllStoreResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String category;
    private final Integer minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public static FindAllStoreResponseDto of(Store store) {
        return new FindAllStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getMinOrderPrice(),
            store.getOpenTime(),
            store.getCloseTime()
        );
    }
}

