package com.gimin.jpopblog.global.config.auth.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class MissingOAuthAttributeException extends OAuth2AuthenticationException {
    public MissingOAuthAttributeException(String message){
        super(new OAuth2Error("invalid_oauth_response"),message);
    }
}
