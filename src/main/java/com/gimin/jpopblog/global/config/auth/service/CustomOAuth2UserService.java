package com.gimin.jpopblog.global.config.auth.service;

import com.gimin.jpopblog.domain.user.entity.Role;
import com.gimin.jpopblog.global.config.auth.dto.OAuthAttributes;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.repository.UserRepository;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    // 1. DefaultOAuth2UserService를 상수로 선언하여 재사용
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    private final HttpSession httpSession;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 위임(Delegation)을 통해 기본적인 사용자 정보 로드
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분 (e.g., "google", "naver", ...)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (PK)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인 플랫폼별 속성을 DTO(OAuthAttributes)로 변환
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //이메일로 기존 사용자인지 신규 사용자인지 확인
        Optional<User> userOptional = userRepository.findByEmail(attributes.getEmail());

        if(userOptional.isPresent()){
           return processExistingUser(userOptional.get(), attributes);
        }else{
           return processNewUser(attributes);
        }
    }
    // 기존 사용자 처리
    public OAuth2User processExistingUser(User user, OAuthAttributes attributes){
        user.update(attributes.getName(),attributes.getPicture());//소셜 프로필 변경사항 업데이트

        httpSession.setAttribute("user",new SessionUser(user));

        // 인증된 사용자 정보를 Spring Security Context에 저장하기 위해 반환
        // 이 과정이 끝나면 SecurityContextHolder에 의해 세션에 사용자 정보가 저장된다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    //신규 사용자 처리
    public OAuth2User processNewUser(OAuthAttributes attributes){
        //2. 신규 사용자인 경우 (가회원 등록)
        // 세션에 소셜 로그인 정보 임시 등록
        httpSession.setAttribute("social_info",new SessionUser(attributes));

        //가회원 권한 부여
        //singleton으로 권한이 하나뿐인 사용자 만듬(요소 하나만 들어있는 불변 Set)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.GUEST.getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}