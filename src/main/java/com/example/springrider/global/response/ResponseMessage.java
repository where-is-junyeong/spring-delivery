package com.example.springrider.global.response;

public enum ResponseMessage {

    PASSWORD_MODIFY_SUCCESS("비밀번호가 성공적으로 변경되었습니다. 로그아웃 처리되었습니다."),
    USER_DELETED("회원 탈퇴가 완료되었습니다."),
    LOGIN_SUCCESS("로그인 성공"),
    LOGOUT_SUCCESS("로그아웃 성공"),
    PROFILE_UPDATE_SUCCESS("프로필이 성공적으로 수정되었습니다."),
    ;

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
