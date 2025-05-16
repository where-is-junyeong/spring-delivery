package com.example.springrider.domain.menu.service;

import com.example.springrider.aop.StoreOwnerCheck;
import com.example.springrider.domain.menu.dto.request.MenuRequestDto;
import com.example.springrider.domain.menu.dto.response.MenuResponseDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 메뉴 등록 서비스
     *
     * @param storeId    메뉴를 등록할 가게
     * @param requestDto 메뉴 정보가 담긴 {@link MenuRequestDto}
     */
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public MenuResponseDto create(Long userId, Long storeId, MenuRequestDto requestDto) {
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);
        Menu menu = new Menu(requestDto);
        menu.setStore(findStore);

        // 메뉴 저장
        menuRepository.save(menu);

        return MenuResponseDto.of(menu);
    }

    /**
     * 메뉴 수정 서비스
     *
     * @param storeId    가게 아이디
     * @param menuId     메뉴 아이디
     * @param userId     유저 아이디 (인터셉터로 처리 예정)
     * @param requestDto 수정할 메뉴 정보가 담긴 {@link MenuRequestDto}
     * @return 수정된 메뉴 정보가 담긴 {@link MenuResponseDto}
     */
    @Transactional
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public MenuResponseDto update(
        Long storeId, Long menuId, Long userId, MenuRequestDto requestDto
    ) {
        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
        findMenu.updateMenu(requestDto);

        return MenuResponseDto.of(findMenu);
    }

    /**
     * 메뉴 삭제 서비스
     *
     * @param storeId 가게 식별자
     * @param menuId  메뉴 식별자
     * @param userId  유저 식별자
     */
    @Transactional
    @StoreOwnerCheck(userIdParam = "userId", storeIdParam = "storeId")
    public MenuResponseDto delete(Long storeId, Long menuId, Long userId) {
        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
        findMenu.deleteMenu();

        return MenuResponseDto.of(findMenu);
    }

}
