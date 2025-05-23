package com.example.springrider.domain.store.service;

import static com.example.springrider.global.exception.ExceptionCode.STORE_INVALID_STATUS_CHANGE;
import static com.example.springrider.global.exception.ExceptionCode.STORE_INVALID_TIME;
import static com.example.springrider.global.exception.ExceptionCode.STORE_USER_MISMATCH;

import com.example.springrider.config.cache.CacheStrategy;
import com.example.springrider.domain.store.dto.request.StoreRequestDto;
import com.example.springrider.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.springrider.domain.store.dto.response.StoreResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.global.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerStoreService {

    private final StoreRepository storeRepository;
    private final CacheStrategy cacheStrategy;
    /**
     * 가게 생성
     *
     * @param storeRequestDto 가게 생성 요청 dto
     * @param user         로그인한 유저의 고유 식별자
     * @return 가게 정보 + 유저 이름을 savedStore에 저장
     */
    @Transactional
    public StoreResponseDto create(StoreRequestDto storeRequestDto, User user) {

        // store에 있는 스태틱 메소드 StoreInfo에 가게정보와 유저의정보(유저이름)을 담고
        // 객체 store 생성
        Store store = Store.StoreInfo(storeRequestDto, user);

        // 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.of(savedStore);
    }

    /**
     * 가게 수정
     *
     * @param storeId    가게 고유 식별자
     * @param requestDto 가게 수정 요청 dto
     * @param user    유저 고유 식별자
     * @return 수정된 가게 정보
     */

    @Transactional
    public StoreResponseDto update(
        Long storeId, UpdateStoreRequestDto requestDto, User user) {

        // 가게가 존재하는지 확인하는 코드
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 지금 로그인한 유저가 사장이 맞는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new InvalidRequestException(STORE_USER_MISMATCH);
        }

        // 상태 변경 로직
        StoreStatus newStatus = requestDto.getStatus();

        // 현재 가게 상태와 입력받은 상태가 다를 때
        if (store.getStatus() != newStatus) {
            switch (newStatus) {
                case ACTIVE -> {
                    store.changeStatus(StoreStatus.ACTIVE);
                }
                // CLOSED 상태에서 또 CLOSED로 변경해도 괜찮음 (수정할 때 store status를 받아야 함)
                case CLOSED -> store.changeStatus(StoreStatus.CLOSED);
                default -> throw new InvalidRequestException(STORE_INVALID_STATUS_CHANGE);
            }
        }

        // 오픈 시간이 마감 시간보다 느릴 때
        if (requestDto.getOpenTime().isAfter(requestDto.getCloseTime())) {
            throw new InvalidRequestException(STORE_INVALID_TIME);
        }

        boolean isNameChanged = !requestDto.getName().isEmpty() && !requestDto.getName().equals(store.getName());

        // 가게 정보 수정
        store.update(requestDto);

        if (isNameChanged){
            cacheStrategy.evictAllSearchCache();
        }

        return StoreResponseDto.of(store);
    }

    @Transactional
    public StoreResponseDto delete(
            Long storeId, User user
    ) {

        // 가게가 존재하는지 확인하는 코드
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 지금 로그인한 유저가 사장이 맞는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new InvalidRequestException(STORE_USER_MISMATCH);
        }

        // 현재 가게 상태와 입력받은 상태가 다를 때
        if (store.getStatus() == StoreStatus.ACTIVE) {
                // CLOSED 상태에서 또 CLOSED로 변경해도 괜찮음 (수정할 때 store status를 받아야 함)
                store.changeStatus(StoreStatus.CLOSED);
        }

        store.delete();

        return StoreResponseDto.of(store);
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
