package com.example.springrider.domain.store.entity;

import static com.example.springrider.global.exception.ExceptionCode.STORE_INVALID_STATUS_CHANGE;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.store.dto.request.StoreRequestDto;
import com.example.springrider.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.global.exception.InvalidRequestException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@BatchSize(size = 100)
@Table(name = "store")
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 35)
    private String category;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    public Store(String name, String address, String category, LocalTime openTime,
        LocalTime closeTime, Integer minOrderPrice, StoreStatus status, User user) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.user = user;
    }

    public static Store StoreInfo(StoreRequestDto storeRequestDto, User user) {
        return new Store(
            storeRequestDto.getName(),
            storeRequestDto.getAddress(),
            storeRequestDto.getCategory(),
            storeRequestDto.getOpenTime(),
            storeRequestDto.getCloseTime(),
            storeRequestDto.getMinOrderPrice(),
            StoreStatus.ACTIVE,
            user
        );
    }

    public void update(UpdateStoreRequestDto dto) {
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.category = dto.getCategory();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.minOrderPrice = dto.getMinOrderPrice();
    }

    public void changeStatus(StoreStatus newStatus) {
        // 상태가 동일한 경우 변경하지 않음
        if (this.status == newStatus) {
            return;  // 상태가 이미 동일하면 아무 작업도 하지 않음
        }

        // 상태가 ACTIVE일 때만 변경 가능
        if (this.status == StoreStatus.CLOSED && newStatus == StoreStatus.ACTIVE) {
            this.status = newStatus;  // 폐업 상태에서만 재오픈 가능
        } else if (this.status != StoreStatus.CLOSED && newStatus == StoreStatus.CLOSED) {
            this.status = newStatus;  // 다른 상태에서만 폐업 상태로 변경
        }
        // 상태 변경 불가능한 경우는 예외 처리
        else {
            throw new InvalidRequestException(STORE_INVALID_STATUS_CHANGE);
        }
    }
}
