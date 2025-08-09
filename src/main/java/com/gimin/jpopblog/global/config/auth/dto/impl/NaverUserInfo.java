package com.gimin.jpopblog.global.config.auth.dto.impl;

import com.gimin.jpopblog.global.config.auth.dto.OAuthUserInfo;
import com.gimin.jpopblog.global.config.auth.exception.MissingOAuthAttributeException;

import java.util.Map;

public class NaverUserInfo implements OAuthUserInfo {
    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        Object res = attributes.get("response");
        if (!(res instanceof Map)) {
            throw new MissingOAuthAttributeException("Naver OAuth 응답에 response가 없습니다.");
        }
        this.response = (Map<String, Object>) res;
    }

    @Override
    public String getName() { return (String)response.get("name"); }

    @Override
    public String getEmail() { return (String)response.get("email"); }

    @Override
    public String getPicture() {
        return (String) response.getOrDefault("profile_image", "");
    }

    private String getRequired(String key) {
        String value = (String) response.get(key);
        if (value == null || value.isBlank()) {
            throw new MissingOAuthAttributeException("Naver OAuth 응답에 " + key + "이 없습니다.");
        }
        return value;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }
}
