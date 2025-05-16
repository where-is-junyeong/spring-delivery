package com.example.springrider.domain.review.repository;

import com.example.springrider.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderId(Long orderId);

    @Query("""
            SELECT r FROM Review r
            JOIN r.order o
            JOIN o.store s
            WHERE s.id = :storeId
        """)
    Page<Review> findAllByStoreId(@Param("storeId") Long storeId, Pageable pageable);
}
