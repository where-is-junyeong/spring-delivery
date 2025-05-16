package com.example.springrider.config.interceptor;

import com.example.springrider.config.Const;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.service.OwnerStoreService;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.service.UserService;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class StoreOwnerInterceptor implements HandlerInterceptor {

    private final OwnerStoreService ownerStoreService;
    private final UserService userService;

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {

        Long userId = (Long) request.getAttribute(Const.SESSION_USER_ID);

        // 로그인 여부 확인
        if (userId == null) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }

        // 사장 권한 확인 및 유저 확인
        User user = userService.findById(userId);
        if (user.getRole() != UserRole.OWNER) {
            throw new AuthException(ExceptionCode.STORE_OWNER_ONLY);
        }

        // storeId 가 가게소유자인지 확인 아니면 예외
        Long storeId = extractStoreId(request);
        if (storeId != null) {
            Store store = ownerStoreService.findEntity(storeId); // 가게를 조회
            if (!store.getUser().getId().equals(user.getId())) {
                throw new AuthException(ExceptionCode.STORE_USER_MISMATCH);
            }
        }

        return true;
    }

    private Long extractStoreId(HttpServletRequest request) {
        String[] parts = request.getRequestURI().split("/");
        if (parts.length >= 4 && "stores".equals(parts[2])) { // storedId 추출
            try {
                return Long.parseLong(parts[3]); // storeId를 숫자로 변환
            } catch (NumberFormatException e) {
                throw new AuthException(ExceptionCode.STORE_NOT_FOUND);
            }
        }
        return null;
    }
}
