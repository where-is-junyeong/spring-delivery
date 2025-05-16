package com.example.springrider.domain.store.service;

import static com.example.springrider.global.exception.ExceptionCode.STORE_ALREADY_CLOSED;

import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.dto.response.FindStoreResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.exception.InvalidRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserStoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 전체 가게 목록 조회 ACTIVE 상태인 가게만 노출
     *
     * @return 간단한 가게 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<FindAllStoreResponseDto> finds() {
        // 영업 중인 가게만 조회 (상태가 CLOSED가 아닌 가게만 조회)
        List<Store> stores = storeRepository.findAllByStatusNot(StoreStatus.CLOSED);

        // 리스트에 담은 stores를 리스트에 담긴 store simple response dto형태로 변환
        return stores.stream()
            .map(FindAllStoreResponseDto::of)
            .toList();
    }

    /**
     * 가게 상세 조회
     *
     * @param storeId 가게 고유 식별자
     * @return 가게 상세 정보를 담은 dto
     */
    public FindStoreResponseDto find(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        // CLOSED된 가게인 경우 예외 처리
        if (store.getStatus() == StoreStatus.CLOSED) {
            throw new InvalidRequestException(STORE_ALREADY_CLOSED);
        }

        // 가게의 정보와 메뉴도 보이게 가게 상세 dto 형태 반환
        return FindStoreResponseDto.of(store);
    }

    /**
     * {@link Store} 엔티티 반환 메소드
     *
     * @param storeId 가게 식별자
     * @return {@link Store}
     */
    public Store findEntity(Long storeId) {
        return storeRepository.findByIdOrElseThrow(storeId);
    }
}
