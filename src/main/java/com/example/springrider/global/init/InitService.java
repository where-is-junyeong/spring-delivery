package com.example.springrider.global.init;

import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "init.enabled", havingValue = "true")
public class InitService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("초기 데이터 생성 시작");

        // 테스트용 사장 생성
        User user = User.builder()
            .email("test@example.com")
            .password(passwordEncoder.encode("password1!"))
            .nickname("테스트계정")
            .role(UserRole.OWNER)
            .isWithdraw(false)
            .build();

        userRepository.save(user);

        // 테스트용 가게 생성
        Store store1 = Store.builder()
            .name("김밥천국")
            .address("서울시 강남구")
            .category("한식")
            .description("테스트 가게 설명")
            .openTime(LocalTime.parse("09:00"))
            .closeTime(LocalTime.parse("21:00"))
            .minOrderPrice(12000)
            .status(StoreStatus.ACTIVE)
            .user(user)
            .build();

        Store store2 = Store.builder()
            .name("파스타집")
            .address("서울시 강남구")
            .category("양식")
            .description("테스트 가게 설명")
            .openTime(LocalTime.parse("09:00"))
            .closeTime(LocalTime.parse("21:00"))
            .minOrderPrice(12000)
            .status(StoreStatus.ACTIVE)
            .user(user)
            .build();

        Store store3 = Store.builder()
            .name("bhc")
            .address("서울시 강남구")
            .category("치킨")
            .description("테스트 가게 설명")
            .openTime(LocalTime.parse("09:00"))
            .closeTime(LocalTime.parse("21:00"))
            .minOrderPrice(12000)
            .status(StoreStatus.ACTIVE)
            .user(user)
            .build();

        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);

        // 테스트용 메뉴 생성
        Menu menu1 = Menu.builder()
            .name("원조김밥")
            .price(4000)
            .description("기본 김밥")
            .category("메인메뉴")
            .store(store1)
            .build();

        Menu menu2 = Menu.builder()
            .name("참치김밥")
            .price(5000)
            .description("참치 김밥")
            .category("메인메뉴")
            .store(store1)
            .build();

        Menu menu3 = Menu.builder()
            .name("돈까스")
            .price(10000)
            .description("돈까스입니다")
            .category("메인메뉴")
            .store(store1)
            .build();

        Menu menu4 = Menu.builder()
            .name("알리오 올리오")
            .price(10000)
            .description("파스타입니다")
            .category("메인메뉴")
            .store(store2)
            .build();

        Menu menu5 = Menu.builder()
            .name("토마토 스파게티")
            .price(1000)
            .description("파스타입니다")
            .category("메인메뉴")
            .store(store2)
            .build();

        Menu menu6 = Menu.builder()
            .name("봉골레 파스타")
            .price(1000)
            .description("파스타입니다")
            .category("메인메뉴")
            .store(store2)
            .build();

        Menu menu7 = Menu.builder()
            .name("후라이드")
            .price(10000)
            .description("치킨입니다")
            .category("메인메뉴")
            .store(store3)
            .build();

        Menu menu8 = Menu.builder()
            .name("양념")
            .price(5000)
            .description("치킨입니다")
            .category("메인메뉴")
            .store(store3)
            .build();

        Menu menu9 = Menu.builder()
            .name("반반")
            .price(10000)
            .description("치킨입니다")
            .category("메인메뉴")
            .store(store3)
            .build();

        menuRepository.save(menu1);
        menuRepository.save(menu2);
        menuRepository.save(menu3);
        menuRepository.save(menu4);
        menuRepository.save(menu5);
        menuRepository.save(menu6);
        menuRepository.save(menu7);
        menuRepository.save(menu8);
        menuRepository.save(menu9);




        log.info("초기 데이터 생성 완료");
    }
}
