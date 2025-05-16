package com.example.springrider.domain.review.service;

import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.domain.order.repository.OrderRepository;
import com.example.springrider.domain.review.dto.request.CreateReviewRequestDto;
import com.example.springrider.domain.review.dto.response.FindAllReviewResponseDto;
import com.example.springrider.domain.review.dto.response.ReviewResponseDto;
import com.example.springrider.domain.review.entity.Review;
import com.example.springrider.domain.review.repository.ReviewRepository;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ReviewResponseDto create(Long orderId, Long userId,
        @Valid CreateReviewRequestDto requestDto) {
        Order order = orderRepository.findByIdWithUser(orderId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.ORDER_NOT_FOUND));
        User findedUser = order.getUser();
        //타인의 주문에 대한 리뷰 생성을 요청한 경우
        if (!findedUser.getId().equals(userId)) {
            throw new InvalidRequestException(ExceptionCode.FORBIDDEN_REQUEST);
        }
        //주문 상태가 배달완료가 아닌 경우
        if (OrderStatus.DELIVERED != order.getStatus()) {
            throw new InvalidRequestException(ExceptionCode.REVIEW_NOT_AVAILABLE_YET);
        }
        //이미 이 주문에 대한 리뷰가 작성된 경우
        if (reviewRepository.existsByOrderId(order.getId())) {
            throw new InvalidRequestException(ExceptionCode.REVIEW_ALREADY_EXISTS);
        }
        order.changeStatus(OrderStatus.REVIEWED);
        Review review = Review.of(requestDto, findedUser, order);
        reviewRepository.save(review);
        return ReviewResponseDto.of(review);
    }

    @Transactional(readOnly = true)
    public FindAllReviewResponseDto findAll(Long storeId, int pageNumber, int size) {
        //PageNUmber가 음수 일때에 대한 방어 코드
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        // PageRequest는 0부터 시작하므로 pageNumber에서 1을 뺀다
        Pageable pageable = PageRequest.of(pageNumber - 1, size);

        if (!storeRepository.existsById(storeId)) {
            throw new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND);
        }

        Page<Review> reviewPage = reviewRepository.findAllByStoreId(storeId, pageable);
        List<Review> reviews = reviewPage.getContent();

        if (reviews.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.REVIEW_NOT_FOUND);
        }

        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
            .map(ReviewResponseDto::of)
            .toList();

        return FindAllReviewResponseDto.of(
            reviews.get(0).getOrder().getStore().getName(),
            reviewResponseDtos
        );
    }
}
