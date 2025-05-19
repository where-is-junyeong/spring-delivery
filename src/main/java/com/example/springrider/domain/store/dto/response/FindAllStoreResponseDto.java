package com.example.springrider.domain.store.dto.response;

import com.example.springrider.domain.store.entity.Store;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllStoreResponseDto {

    private Long id;
    private String name;
    private String address;
    private String category;
    private String description;
    private Integer minOrderPrice;
    private LocalTime openTime;
    private LocalTime closeTime;

    public static FindAllStoreResponseDto of(Store store) {
        return new FindAllStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getDescription(),
            store.getMinOrderPrice(),
            store.getOpenTime(),
            store.getCloseTime()
        );
    }
}

