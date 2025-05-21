package com.example.springrider.domain.store.repository;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.user.entity.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    long countByUser(User user); // 사장님이 등록한 가게 개수

    long countByUserAndStatus(User currentUser, StoreStatus storeStatus);

    List<Store> findAllByStatusNot(StoreStatus status);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }

    @EntityGraph(attributePaths = {"menus"})
    @Query("select distinct s " +
            "from Store s " +
            "left join s.menus m " +
            "where s.name like %:keyword% " +
            "or m.name like %:keyword%")
    Page<Store> SearchPageByKeyword(@Param("keyword")String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"menus"})
    @Query("select distinct s " +
            "from Store s " +
            "left join s.menus m " +
            "where s.name like %:keyword% " +
            "or m.name like %:keyword%")
    List<Store> SearchByKeyword(String keyword);
}
