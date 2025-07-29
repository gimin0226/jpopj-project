package com.gimin.jpopblog.global.config.auth.handler;

import com.gimin.jpopblog.domain.user.entity.Role;
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

        // 한 명의 사용자가 가진 권한 목록을 순회
        // 하나라도 만족하면 true 반환하고 스트림 멈춤
        boolean isGuest = oAuth2User.getAuthorities().stream()
                .anyMatch(a-> a.getAuthority().equals(Role.GUEST.getKey()));

        if(isGuest){
            // 권한이 GUEST이면 추가 정보 입력 페이지로 리다이렉트
            response.sendRedirect("/signup-more-info");
        }else {
            // 권한이 USER이면 메인 페이지로 리다이렉트
            response.sendRedirect("/");
        }

    }

}