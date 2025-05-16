package com.example.springrider.domain.review.controller;


import com.example.springrider.config.Const;
import com.example.springrider.domain.review.dto.request.CreateReviewRequestDto;
import com.example.springrider.domain.review.dto.response.FindAllReviewResponseDto;
import com.example.springrider.domain.review.dto.response.ReviewResponseDto;
import com.example.springrider.domain.review.service.ReviewService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/customers/orders/{orderId}/reviews")
    public ApiResponse<ReviewResponseDto> create(
        @PathVariable Long orderId,
        @Valid @RequestBody CreateReviewRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.created(reviewService.create(orderId, userId, requestDto));
    }

    @GetMapping("/customers/stores/{storeId}/reviews/page/{pageNumber}")
    public ApiResponse<FindAllReviewResponseDto> findAll(
        @PathVariable Long storeId,
        @PathVariable int pageNumber,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.ok(reviewService.findAll(storeId, pageNumber, size));
    }
}
