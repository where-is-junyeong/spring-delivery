package com.example.springrider.domain.store.dto.response;

import com.example.springrider.domain.menu.dto.response.MenuResponseDto;
import com.example.springrider.domain.store.entity.Store;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindStoreResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String category;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Integer minOrderPrice;
    private final List<MenuResponseDto> menus;

    public static FindStoreResponseDto of(Store store) {
        List<MenuResponseDto> menuDtos = store.getMenus().stream()
            .map(MenuResponseDto::of)
            .collect(Collectors.toList());

        return new FindStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getCategory(),
            store.getOpenTime(),
            store.getCloseTime(),
            store.getMinOrderPrice(),
            menuDtos
        );
    }
}