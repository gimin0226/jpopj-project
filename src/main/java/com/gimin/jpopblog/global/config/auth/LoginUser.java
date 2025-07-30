package com.gimin.jpopblog.global.config.auth;

import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface   LoginUser {
    //HttpSession에서 가져올 속성(attribute)의 키 값을 지정한다.
    @NotNull
    String value();
}
