package com.example.springrider.aop;

import com.example.springrider.domain.order.dto.response.OrderResponseDto;
import com.example.springrider.domain.order.service.UserOrderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventLogAspect {

    private final UserOrderService userOrderService;

    @Around("@annotation(OrderEventLog)")
    public Object logOrderCreate(ProceedingJoinPoint joinPoint) throws Throwable {

        LocalDateTime requestTime = LocalDateTime.now();
        Object result = joinPoint.proceed();

        if (result instanceof OrderResponseDto responseDto) {
            Long orderId = responseDto.getOrderId();
            Long storeId = userOrderService.findStoreIdFromOrder(orderId);

            log.info("[주문 생성] 시간={}, 가게번호={}, 주문번호={}", requestTime, storeId, orderId);
        }

        return result;
    }

}
