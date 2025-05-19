package com.example.springrider.domain.search.dto.response;

import com.example.springrider.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class SearchResponseDto {
    private final Long id;
    private final String name;
    private final String address;
    private final String category;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Integer minOrderPrice;

    public static SearchResponseDto of(Store store) {
        return new SearchResponseDto(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice()
        );
    }
}
