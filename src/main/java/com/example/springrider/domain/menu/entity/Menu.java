package com.example.springrider.domain.menu.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.menu.dto.request.MenuRequestDto;
import com.example.springrider.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Table(name = "menu",
        indexes = {@Index(name = "idx_menu_name", columnList = "name")})
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, length = 35)
    private String category;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")

    private Store store;

    // 메뉴 이름, 가격, 설명 생성자
    public Menu(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
        this.isDeleted = false;
    }

    public void updateMenu(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
    }

    public void deleteMenu() {
        this.isDeleted = true;
    }

    public void updateStore(Store store){
        this.store=store;
    }
}
