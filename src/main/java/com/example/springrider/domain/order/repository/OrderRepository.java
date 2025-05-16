package com.example.springrider.domain.order.repository;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.domain.order.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.menu
        WHERE o.id = :orderId
        """)
    Optional<Order> findByIdWithOrderItemsAndMenu(@Param("orderId") Long orderId);

    default Order findByIdOrElseThrow(Long orderId) {
        return findById(orderId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.ORDER_NOT_FOUND));
    }

    @Query("""
        SELECT o FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.menu m
        JOIN FETCH o.store s
        WHERE o.user.id = :userId
        """)
    List<Order> findAllByUserIdWithOrderItemsAndMenuAndStore(@Param("userId") Long userId);


    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.menu m
        WHERE o.store.id = :storeId
        """)
    List<Order> findAllByStoreIdWithOrderItemsAndMenu(@Param("storeId") Long storeId);

    @Query("""
        SELECT o FROM Order o
        JOIN FETCH o.user
        WHERE o.id = :orderId
        """)
    Optional<Order> findByIdWithUser(@Param("orderId") Long orderId);
}
