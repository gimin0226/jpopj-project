package com.gimin.jpopblog.global.config.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 사용자가 직접 정의한 Argument Resolver
     * 이 Resolver는 특정 조건(예: @LoginUser 어노테이션)을 만족하는 컨트롤러 파라미터를 처리하는 로직을 담고 있다.
     */
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * WebMvcConfigurer 인터페이스의 메소드를 오버라이드(재정의)하여
     * Spring MVC의 Argument Resolver 목록에 커스텀 Resolver를 추가하는 역할을 한다.
     * @param argumentResolvers Spring MVC가 관리하는 Argument Resolver의 리스트
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // Spring이 관리하는 리스트에 직접 만든 loginUserArgumentResolver를 추가한다.
        // 이로써 Spring은 컨트롤러의 파라미터를 해석할 때 우리가 만든 Resolver도 사용하게 된다.
        argumentResolvers.add(loginUserArgumentResolver);
    }
}