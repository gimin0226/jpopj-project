package com.gimin.jpopblog.global.config.auth.dto;

public enum OAuthProvider {
    GOOGLE, NAVER, KAKAO;

    public static OAuthProvider from(String registrationId){
        return switch(registrationId.toLowerCase()){
            case "google"-> GOOGLE;
            case "naver" -> NAVER;
            case "kakao" -> KAKAO;
            default -> throw new IllegalArgumentException("잘못된 OAuth 제공자: "+ registrationId);

        };
    }
}
