package com.example.springrider.domain.order.enums;

public enum OrderStatus {
    PENDING,            // 주문 수락 대기 중
    RECEIVED,           // 식당이 주문을 수락
    COOKING,            // 조리 중
    DELIVERING,          // 배달 중
    DELIVERED,          // 배달 완료 (고객 수령)
    REVIEWED,          // 리뷰 작성 완료
    CANCELED          // 주문 취소
}
