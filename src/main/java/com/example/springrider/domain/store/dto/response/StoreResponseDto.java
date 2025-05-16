package com.example.springrider.domain.store.dto.response;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String category;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Integer minOrderPrice;
    private final StoreStatus status;
    private final String ownerName;

    public static StoreResponseDto of(Store store) {
        return new StoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getMinOrderPrice(),
            store.getStatus(),
            store.getUser().getName()  // 여기서 바로 유저 이름 꺼내옴
        );
    }
}
