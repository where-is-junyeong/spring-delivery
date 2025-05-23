package com.example.springrider.domain.search.dto.Response;

import com.example.springrider.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponseDto {

    private Long id;
    private String name;
    private String address;
    private String category;
    private Integer minOrderPrice;
    private LocalTime openTime;
    private LocalTime closeTime;

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
