package com.example.springrider.domain.menu.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.menu.dto.request.MenuRequestDto;
import com.example.springrider.domain.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "menu")
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 20)
    private String contents;

    @Column(nullable = false, length = 35)
    private String category;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Setter
    private Store store;

    // 메뉴 이름, 가격, 설명 생성자
    public Menu(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.contents = requestDto.getContents();
        this.category = requestDto.getCategory();
        this.isDeleted = false;
    }

    public void updateMenu(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.contents = requestDto.getContents();
        this.category = requestDto.getCategory();
    }

    public void deleteMenu() {
        this.isDeleted = true;
    }

}
