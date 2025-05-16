package com.example.springrider.aop;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.service.OwnerStoreService;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StoreOwnerCheckAspect {

    private final OwnerStoreService storeService;

    @Before("@annotation(storeOwnerCheck)")
    public void checkStoreOwner(JoinPoint joinPoint, StoreOwnerCheck storeOwnerCheck) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        Long userId = null;
        Long storeId = null;

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(storeOwnerCheck.userIdParam())) {
                userId = (Long) args[i];
            }
            if (paramNames[i].equals(storeOwnerCheck.storeIdParam())) {
                storeId = (Long) args[i];
            }
        }

        if (userId == null || storeId == null) {
            throw new IllegalArgumentException("userId 나 storeId를 찾을 수 없습니다.");
        }

        Store store = storeService.findEntity(storeId);
        if (!Objects.equals(store.getUser().getId(), userId)) {
            throw new AuthException(ExceptionCode.STORE_ACCESS_DENIED);
        }
    }

}
