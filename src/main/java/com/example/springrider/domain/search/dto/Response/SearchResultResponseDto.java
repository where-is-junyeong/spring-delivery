package com.example.springrider.domain.search.dto.Response;

import com.example.springrider.domain.store.entity.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class SearchResultResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String category;
    private final Integer minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public static SearchResultResponseDto from(Store store){
        return new SearchResultResponseDto(
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
