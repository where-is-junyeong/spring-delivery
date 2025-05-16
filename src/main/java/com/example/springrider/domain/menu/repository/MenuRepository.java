package com.example.springrider.domain.menu.repository;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByIdOrElseThrow(Long menuId) {
        return findById(menuId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));
    }
}
