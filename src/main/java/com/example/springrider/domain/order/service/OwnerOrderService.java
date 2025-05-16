package com.example.springrider.domain.order.service;

import com.example.springrider.aop.StoreOwnerCheck;
import com.example.springrider.domain.order.dto.request.CancelOrderRequestDto;
import com.example.springrider.domain.order.dto.request.UpdateOrderStatusRequestDto;
import com.example.springrider.domain.order.dto.response.CancelOrderResponseDto;
import com.example.springrider.domain.order.dto.response.OwnerOrderResponseDto;
import com.example.springrider.domain.order.dto.response.UpdateOrderStatusResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderCancelReason;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.global.security.DefaultPasswordEncoder;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final DefaultPasswordEncoder defaultPasswordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public UpdateOrderStatusResponseDto update(
        Long orderId, Long storeId, Long userId,
        @Valid UpdateOrderStatusRequestDto requestDto
    ) {
        Order order = orderRepository.findByIdOrElseThrow(orderId);
        order.changeStatus(OrderStatus.valueOf(requestDto.getStatus()));
        return UpdateOrderStatusResponseDto.of(order);
    }

    @Transactional(readOnly = true)
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public List<OwnerOrderResponseDto> findAll(Long storeId, Long userId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        List<Order> orders = orderRepository.findAllByStoreIdWithOrderItemsAndMenu(storeId);

        return List.of(OwnerOrderResponseDto.of(store, orders));
    }

    @Transactional
    public CancelOrderResponseDto delete(
        Long storeId, Long orderId,
        Long userId, @Valid CancelOrderRequestDto requestDto) {
        //비밀번호 일치 여부 검증
        User user = userRepository.findById(userId).orElseThrow();
        if (!defaultPasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
        //취소 사유가 other(기타)일 경우에는 취소 메세지는 필수 값 -> 이를 검증
        String cancelReason = requestDto.getCancelReason();
        String cancelMessage = requestDto.getCancelMessage();
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new InvalidRequestException(
                ExceptionCode.ORDER_NOT_FOUND));
        if ("other".equals(cancelReason.toLowerCase(Locale.ROOT))) {
            if (isMessageNullOrBlank(cancelMessage)) {
                throw new InvalidRequestException(
                    ExceptionCode.REQUIRED_ORDER_CANCEL_MESSAGE_FOR_OTHER);
            }
        }
        //update dirty checking
        order.changeStatus(OrderStatus.CANCELED);
        order.setOrderCancelReason(OrderCancelReason.valueOf(cancelReason));
        order.setCancelMessage(cancelMessage);
        //dto 반환
        return CancelOrderResponseDto.of(order);
    }

    //메세지 null 여부 검증 메서드
    public boolean isMessageNullOrBlank(String message) {
        return message == null || message.isBlank();
    }
}