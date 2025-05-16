package com.example.springrider.domain.order.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.order.enums.OrderCancelReason;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
@BatchSize(size = 100)
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_user", columnList = "user_id"),
    @Index(name = "idx_order_store", columnList = "store_id")
}
)
public class Order extends BaseEntity {

    private Integer totalPrice;

    @Column(nullable = false, length = 100)
    @Setter
    private String deliveryAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    @Setter
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderCancelReason cancelReason;

    @Column(length = 20)
    private String cancelMessage;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void add(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public void calculateTotalPrice() {
        int total = 0;
        for (OrderItem item : this.orderItems) {
            total += item.getMenu().getPrice() * item.getQuantity();
        }
        this.totalPrice = total;
    }

    //상태 수정을 위한 클래스 내부 메서드
    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderCancelReason(OrderCancelReason cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setCancelMessage(String message) {
        this.cancelMessage = message;
    }
}
