package com.example.springrider.domain.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.springrider.domain.menu.dto.request.MenuRequestDto;
import com.example.springrider.domain.menu.dto.response.MenuResponseDto;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    private Store store;
    private Menu menu;
    private MenuRequestDto menuRequestDto;

    @BeforeEach
    void setUp() {
        store = new Store();
        menuRequestDto = new MenuRequestDto("레전드 전주 비빔밥", 8000, "레전드 고추장이 쓰까묵는 비빔빱", "한식");
        menu = new Menu(menuRequestDto);
        menu.setStore(store);
    }

    @AfterEach
    void afterEach() {
        System.out.println("테스트 코드 성공했습니동동");
    }

    @Test
    void menu_create_메뉴가_정상적으로_생성된다() {
        // given
        given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponseDto result = menuService.create(1L, 1L, menuRequestDto);

        // then
        assertEquals(menuRequestDto.getName(), result.getName());
        assertEquals(menuRequestDto.getPrice(), result.getPrice());
        assertEquals(menuRequestDto.getContents(), result.getContents());
        assertEquals(menuRequestDto.getCategory(), result.getCategory());
        assertEquals(false, result.getIsDeleted());
    }

    @Test
    void menu_update_메뉴를_성공적으로_수정했다() {
        // given
        given(menuRepository.findByIdOrElseThrow(1L)).willReturn(menu);

        // when
        MenuResponseDto result = menuService.update(1L, 1L, 1L, menuRequestDto);

        // then
        assertEquals(menuRequestDto.getName(), result.getName());
        assertEquals(menuRequestDto.getPrice(), result.getPrice());
        assertEquals(menuRequestDto.getContents(), result.getContents());
        assertEquals(menuRequestDto.getCategory(), result.getCategory());
    }

    @Test
    void menu_delete_메뉴를_성공적으로_삭제했다() {
        // given
        given(menuRepository.findByIdOrElseThrow(1L)).willReturn(menu);

        // when
        MenuResponseDto result = menuService.delete(1L, 1L, 1L);

        // then
        assertEquals(true, result.getIsDeleted());
    }
}