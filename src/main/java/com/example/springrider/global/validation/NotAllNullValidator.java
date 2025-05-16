package com.example.springrider.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class NotAllNullValidator implements ConstraintValidator<NotAllNull, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; //객체 자체가 null이면 유효하지 않음
        }
        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);//필드 강제 접근 허용
            try {
                if (field.get(value) != null) {
                    return true; // 하나라도 값이 있으면 유효
                }
                //field.get() 메서드는 컴파일러 상으로는 IllegalAccessException이 반드시 발생할 수 있다고 보는
                //checked exception이기 때문에 무조건 try-catch 해야만 컴파일이 된다.
                //강제 접근 허용을 했기 때문에 아래의 IllegalAccessException은 거의 의미가 없고 catch 내용을 비움으로써 무시가 된다.
                //변수 명을 ignored로 표현하는게 자바 관례이다.
            } catch (IllegalAccessException ignored) {
            }
        }
        return false; // 모든 필드가 null이면 유효하지 않음
    }
}
