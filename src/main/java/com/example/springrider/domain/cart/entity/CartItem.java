package com.example.springrider.domain.cart.entity;

import com.example.springrider.domain.cart.enums.CartItemStatus;
import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cart_items", indexes = {

    //userId와 수정일, userId와 status Index로 사용할 수 있게 설정
    @Index(name = "idx_user_modified", columnList = "user_id, modified_at"),
    @Index(name = "idx_user_modified_status", columnList = "user_id, modified_at, status")},

    //로직 차원에서 중복 요청의 경우 수량의 변경으로 처리할 예정이지만 문제는 그것이 서버에서 완전히 동시 요청으로 처리될 경우,
    //애플리케이션 로직으로는 감지할 수 없는 경우가 발생 할 수 있으므로 DB차원에서 중복 값의 저장을 방지
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_menu", columnNames = {"user_id", "menu_id"})
    }
)
public class CartItem extends BaseEntity {

    //장바구니 생성시 비교용 가게Id 스냅샷 필드
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    //optional = false는 jpa 차원에서 영속성 순간에 user가 비영속 관계일 경우 TransientPropertyValueException이 발생
    //nullable = false는 DDL 생성 시 user_id NOT NULL → DB에 null 넣으면 ConstraintViolationException 발생
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Integer quantity;

    //장바구니의 상태는 CartItemStatus enum에서 관리하는 상수로 지정
    //SELECT 상태의 장바구니 항목만 주문 가능
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartItemStatus status;

    //생성자
    public CartItem(User user, Menu menu, int quantity) {
        this.user = user;
        this.menu = menu;
        this.quantity = quantity;
        this.storeId = menu.getStore().getId();
        this.status = CartItemStatus.SELECT;
    }

    //수량 수정을 위한 클래스 내부 메서드
    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    //상태 수정을 위한 클래스 내부 메서드
    public void changeStatus(CartItemStatus status) {
        this.status = status;
    }

    //선택 상태인지 여부를 판단하는 메서드
    public boolean isActive() {
        return this.status.equals(CartItemStatus.SELECT);
    }
}