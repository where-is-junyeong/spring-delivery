package com.example.springrider.domain.review.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.review.dto.request.CreateReviewRequestDto;
import com.example.springrider.domain.review.enums.Rating;
import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Column(length = 50)
    private String contents;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    public static Review of(CreateReviewRequestDto requestDto, User user, Order order) {
        return new Review(
            requestDto.getContents(), Rating.valueOf(requestDto.getRating()), user, order
        );
    }
}
