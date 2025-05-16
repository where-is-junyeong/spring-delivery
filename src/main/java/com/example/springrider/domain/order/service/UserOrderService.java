package com.example.springrider.domain.order.service;

import com.example.springrider.aop.OrderEventLog;
import com.example.springrider.domain.cart.entity.CartItem;
import com.example.springrider.domain.cart.repository.CartRepository;
import com.example.springrider.domain.cart.service.CartService;
import com.example.springrider.domain.order.dto.request.CreateOrderRequestDto;
import com.example.springrider.domain.order.dto.response.OrderResponseDto;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.entity.OrderItem;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    /**
     * 주문 요청 서비스
     *
     * @param requestDto 배달 주소 정보가 담긴 {@link CreateOrderRequestDto}
     * @param userId     유저 식별자
     * @return 생성된 주문 정보가 담긴 {@link OrderResponseDto}
     */
    @OrderEventLog
    @Transactional
    public OrderResponseDto create(CreateOrderRequestDto requestDto, Long userId) {
        // 1. 장바구니 메뉴 조회
        List<CartItem> cartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, LocalDateTime.now().minusDays(1)
        );

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.EMPTY_CART);
        }

        // 2. 사용자와 가게 정보 조회
        User user = userRepository.findByIdOrElseThrow(userId);
        Store store = cartItems.get(0).getMenu().getStore();

        // 3. 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        order.setDeliveryAddress(requestDto.getAddress());
        order.setStatus(OrderStatus.PENDING);

        // 4. 장바구니 아이템을 주문 아이템으로 변환
        for (CartItem cartItem : cartItems) {
            if (!cartItem.isActive()) {
                continue;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setQuantity(cartItem.getQuantity());

            order.add(orderItem);
        }

        order.calculateTotalPrice();

        // 5. 주문 저장
        Order savedOrder = orderRepository.save(order);
        Order orderWithItems = orderRepository.findByIdWithOrderItemsAndMenu(savedOrder.getId())
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.ORDER_NOT_FOUND));

        // 6. 장바구니 초기화
        String startTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[주문/장바구니 초기화] userId : {} | 시작 : {}", userId, startTimeStamp);
        cartService.processDelete(cartItems);
        String endTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[주문/장바구니 초기화] userId : {} | 정상 종료 : {}", userId, endTimeStamp);
        return OrderResponseDto.of(orderWithItems);
    }

    /**
     * 주문 목록 조회 서비스
     *
     * @param userId 유저 식별자
     * @return 주문 목록 정보가 담긴 {@link OrderResponseDto}
     */
    public List<OrderResponseDto> findAll(Long userId) {
        List<Order> orders = orderRepository.findAllByUserIdWithOrderItemsAndMenuAndStore(userId);
        return orders.stream()
            .map(OrderResponseDto::of)
            .toList();
    }

    public Long findStoreIdFromOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .map(order -> order.getStore().getId())
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }

}
