package com.example.springrider.domain.order.enums;

public enum OrderCancelReason {
    CUSTOMER_REQUEST,     //고객 요청
    OUT_OF_STOCK,         //재고 부족
    STORE_CLOSED,         //매장 영업 종료
    DELIVERY_UNAVAILABLE, //배달 불가 지역
    ORDER_ERROR,          //주문 내용 오류
    PAYMENT_FAILURE,      //결제 실패
    OTHER                 //기타
}
