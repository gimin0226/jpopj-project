package com.gimin.jpopblog.global.config.auth.handler;

import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 기본 oAuth2User 객체
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String,Object> attributes = oAuth2User.getAttributes();

        //기본적으로 OAuthAttributes에 의해 email은 항상 포함되도록 설계돼 있음
        String email = (String)attributes.get("email");

        // DB 조회 or 저장
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalStateException("소셜 로그인 사용자 정보가 없습니다."));

        // 세션 저장
        httpSession.setAttribute("user", new SessionUser(user));

        // 리다이렉트
        response.sendRedirect("/");

    }

}