package com.gimin.jpopblog.global.config.auth;

import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter){
        // 1. 현재 파라미터에 @LoginUser 에노테이션이 붙어 있는지 확인한다.
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;

        // 2. 현재 파라미터의 타입이 SessionUser 클래스와 정확히 일치하는지 확인한다.
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        // 1. 피라미터에 붙어있는 @LoginUser 에노테이션 객체를 가져온다.
        LoginUser loginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class);

        // 2. 에노테이션의 value() 값(세션 키)을 가져온다.
        // ex) (@LoginUser("admin") Session adminUser) --> sessionKey == "admin"
        String sessionKey = loginUserAnnotation.value();

        // 3. 동적으로 얻은 세션 키를 사용해 HttpSession에서 값을 가져온다.
        return httpSession.getAttribute(sessionKey);
    }
}
