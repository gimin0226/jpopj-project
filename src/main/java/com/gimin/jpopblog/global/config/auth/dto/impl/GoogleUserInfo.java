package com.gimin.jpopblog.global.config.auth.dto.impl;

import com.gimin.jpopblog.global.config.auth.dto.OAuthUserInfo;
import com.gimin.jpopblog.global.config.auth.exception.MissingOAuthAttributeException;

import java.util.Map;

public class GoogleUserInfo implements OAuthUserInfo {
    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return getRequired("name");
    }

    @Override
    public String getEmail() {
        return getRequired("email");
    }

    @Override
    public String getPicture() {
        return (String) attributes.getOrDefault("picture", "");
    }

    private String getRequired(String key) {
        String value = (String) attributes.get(key);
        if (value == null || value.isBlank()) {
            throw new MissingOAuthAttributeException("Google OAuth 응답에 " + key + "이 없습니다.");
        }
        return value;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
