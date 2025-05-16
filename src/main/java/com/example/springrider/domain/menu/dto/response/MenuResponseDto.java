package com.example.springrider.domain.menu.dto.response;

import com.example.springrider.domain.menu.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuResponseDto {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String contents;
    private final String category;
    private final Boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static MenuResponseDto of(Menu menu) {
        return new MenuResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getContents(),
            menu.getCategory(),
            menu.getIsDeleted(),
            menu.getCreatedAt(),
            menu.getModifiedAt()
        );
    }

}
