package com.example.springrider.config;

public class Const {

    // 세션 키
    public static final String SESSION_USER_ID = "userId";

    // URI
    public static final String LOGIN_URI = "/api/users/login";
    public static final String SIGNUP_URI = "/api/users/signup";
    public static final String LOGOUT_URI = "/api/users/logout";
    public static final String WITHDRAW_URI = "/api/users/withdraw";
    public static final String PASSWORD_URI = "/api/users/password";

    private Const() {  // 인스턴스화 못하게 방지

    }

}
