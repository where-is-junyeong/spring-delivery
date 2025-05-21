package com.example.springrider.domain.search.dto.response;

import com.example.springrider.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    private Long id;
    private String name;
    private String address;
    private String category;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minOrderPrice;

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
