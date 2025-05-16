package com.example.springrider.domain.cart.service;

import com.example.springrider.domain.cart.dto.CreateFailedDto;
import com.example.springrider.domain.cart.dto.CreateSuccessDto;
import com.example.springrider.domain.cart.dto.request.CreateCartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.request.CreateCartItemRequestDto;
import com.example.springrider.domain.cart.dto.request.UpdateCartItemRequestDto;
import com.example.springrider.domain.cart.dto.response.CreateCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.response.FindCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.response.FindCartItemResponseDto;
import com.example.springrider.domain.cart.dto.response.UpdateCartItemResponseDto;
import com.example.springrider.domain.cart.entity.CartItem;
import com.example.springrider.domain.cart.enums.CartItemStatus;
import com.example.springrider.domain.cart.repository.CartRepository;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCartItemBulkResponseDto create(Long userId,
        CreateCartItemBulkRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_NOT_FOUND));
        Long requestStoreId = requestDto.getStoreId();
        LocalDateTime limit = LocalDateTime.now().minusDays(1);

        List<CartItem> existingCartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, limit);
        validateExistingCart(existingCartItems, requestStoreId);

        List<CreateSuccessDto> successItems = new ArrayList<>();
        List<CreateFailedDto> failedItems = new ArrayList<>();

        for (CreateCartItemRequestDto createCartItemRequestDto : requestDto.getCartItems()) {
            processCreation(user, requestStoreId, existingCartItems, createCartItemRequestDto,
                successItems,
                failedItems);
        }
        return new CreateCartItemBulkResponseDto(successItems, failedItems);
    }

    private void validateExistingCart(List<CartItem> existingCartItems, Long storeId) {
        if (!existingCartItems.isEmpty() && !existingCartItems.get(0).getStoreId()
            .equals(storeId)) {
            throw new InvalidRequestException(ExceptionCode.CART_STORE_MISMATCH);
        }
    }

    private void processCreation(
        User user,
        Long requestStoreId,
        List<CartItem> existingCartItems,
        CreateCartItemRequestDto createCartItemRequestDto,
        List<CreateSuccessDto> successItems,
        List<CreateFailedDto> failedItems
    ) {
        try {
            Menu menu = menuRepository.findById(createCartItemRequestDto.getMenuId())
                .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));

            if (!menu.getStore().getId().equals(requestStoreId)) {
                throw new InvalidRequestException(ExceptionCode.MENU_STORE_MISMATCH);
            }

            CartItem existing = existingCartItems.stream()
                .filter(ci -> ci.getMenu().getId().equals(createCartItemRequestDto.getMenuId()))
                .findFirst()
                .orElse(null);

            if (existing != null) {
                existing.updateQuantity(
                    existing.getQuantity() + createCartItemRequestDto.getQuantity());
                successItems.add(
                    new CreateSuccessDto(existing.getId(), createCartItemRequestDto.getMenuId(),
                        existing.getQuantity()));
            } else {
                CartItem newItem = new CartItem(user, menu,
                    createCartItemRequestDto.getQuantity());
                CartItem saved = cartRepository.save(newItem);
                successItems.add(
                    new CreateSuccessDto(saved.getId(), createCartItemRequestDto.getMenuId(),
                        createCartItemRequestDto.getQuantity()));
            }

        } catch (InvalidRequestException e) {
            failedItems.add(
                new CreateFailedDto(createCartItemRequestDto.getMenuId(),
                    e.getExceptionCode().name(),
                    e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException(ExceptionCode.CART_DUPLICATE_ITEM);
        }
    }

    @Transactional(readOnly = true)
    public FindCartItemBulkResponseDto findAll(Long userId) {
        LocalDateTime limit = LocalDateTime.now().minusDays(1);
        List<CartItem> cartItems = cartRepository.findAllbyUserIdAndModifiedAtAfterWithMenu(userId,
            limit);

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.EMPTY_CART);
        }

        List<FindCartItemResponseDto> responseDtos = cartItems.stream()
            .map(FindCartItemResponseDto::of)
            .toList();

        int sumTotalPrice = sumTotalPrice(responseDtos);

        return FindCartItemBulkResponseDto.of(responseDtos, cartItems.get(0).getStoreId(),
            sumTotalPrice);
    }

    public int sumTotalPrice(List<FindCartItemResponseDto> responseDtos) {
        return responseDtos.stream()
            .mapToInt(d -> d.getPrice() * d.getQuantity())
            .sum();
    }

    @Transactional
    public UpdateCartItemResponseDto update(
        Long cartItemId, UpdateCartItemRequestDto requestDto, Long userId) {
        LocalDateTime limit = LocalDateTime.now().minusDays(1);
        CartItem cartItem = cartRepository.findByIdAndModifiedAtAfterWithUser(cartItemId, limit)
            .orElseThrow(() -> new InvalidRequestException(
                ExceptionCode.CARTITEM_NOT_FOUND));
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new InvalidRequestException(ExceptionCode.FORBIDDEN_REQUEST);
        }
        CartItemStatus cartItemStatus = CartItemStatus.valueOf(requestDto.getStatus());
        if (isValidQuantity(requestDto) && !hasStatus(requestDto)) {
            cartItem.updateQuantity(requestDto.getQuantity());
        }
        if (isValidQuantity(requestDto) && hasStatus(requestDto)) {
            cartItem.changeStatus(cartItemStatus);
        }
        if (isValidQuantity(requestDto) && hasStatus(requestDto)) {
            cartItem.updateQuantity(requestDto.getQuantity());
            cartItem.changeStatus(cartItemStatus);
        }
        return UpdateCartItemResponseDto.of(cartItem);
    }

    public boolean isValidQuantity(UpdateCartItemRequestDto requestDto) {
        Integer quantity = requestDto.getQuantity();
        return quantity != null && quantity > 0;
    }

    public boolean hasStatus(UpdateCartItemRequestDto requestDto) {
        return requestDto.getStatus() != null;
    }

    //장바구니 삭제 api 전용 메서드
    @Transactional
    public void delete(Long userId) {
        LocalDateTime limit = LocalDateTime.now().minusDays(1);

        List<CartItem> cartItems = cartRepository.findByUserIdAndModifiedAtAfterAndStatus(
            userId, limit, CartItemStatus.SELECT);
        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.EMPTY_CART);
        }
        String startTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[사용자 임의 장바구니 삭제] userId : {} | 시작 : {}", userId, startTimeStamp);
        processDelete(cartItems);
        String endTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[사용자 임의 장바구니 삭제] userId : {} | 정상 종료 : {}", userId, endTimeStamp);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldCartItem() {
        String startTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[장바구니 정기 초기화] | 시작 : {}", startTimeStamp);
        LocalDateTime limit = LocalDateTime.now().minusDays(1);
        List<CartItem> cartItems = cartRepository.findByModifiedAtBefore(limit);
        if (cartItems.isEmpty()) {
            return;
        }
        processDelete(cartItems);
        String endTimeStamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("[장바구니 정기 초기화] | 정상 종료 : {}", endTimeStamp);
    }

    //실제 repository와 연동하는 장바구니 삭제 메서드
    public void processDelete(List<CartItem> cartItems) {
        int errorCount = 0;
        for (CartItem item : cartItems) {
            try {
                cartRepository.delete(item);
            } catch (Exception e) {
                String timeStamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                log.warn("[Fail] | userId : {} cartItemId : {} message : {} timestamp : {}",
                    item.getUser().getId(), item.getId(), e.getMessage(), timeStamp);
                errorCount++;
            }
        }
        if (errorCount > 0) {
            throw new InvalidRequestException(ExceptionCode.CARTITEM_DELETE_FAIL);
        }
    }
}