package com.gimin.jpopblog.global.config.auth.dto;

import java.util.Map;

public interface OAuthUserInfo {
    String getName();
    String getEmail();
    String getPicture();
    Map<String, Object> getAttributes();
}
