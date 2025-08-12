package com.gimin.jpopblog.global.config.auth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.HashMap;
import java.util.Map;

/**
 * ✅ OAuth2 로그인 요청 시 Authorization Request를 가로채서
 * provider에 따라 prompt 파라미터를 추가하는 Resolver
 * (Google: 항상  계정 선택 / Naver: 항상 로그인)
 */
@RequiredArgsConstructor
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    // 내부에서 기본 OAuth2 요청 생성 로직을 위임할 기본 Resolver
    private final OAuth2AuthorizationRequestResolver defaultResolver;

    // 생성자: 클라이언트 등록 정보와 기본 요청 URI("/oauth2/authorization")를 받아 Default Resolver 구성
    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String baseUri) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, baseUri);
    }

    // 요청을 기반으로 AuthorizationRequest를 생성 (기본 요청을 커스터마이징)
    // "/oauth2/authorization/google" 과 같이 경로가 넘어오는 경우
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request); // 기본 요청 생성
        return customizeRequest(authorizationRequest); // 커스터마이징된 요청 리턴
    }

    // 클라이언트 ID를 명시적으로 지정한 경우 처리
    // 사용자가 provider를 직접 선택해서 별도로 registrationId가 선택되어 리다이렉트 하는 경우
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeRequest(authorizationRequest); // 커스터마이징된 요청 리턴
    }

    // ✅ 실제 커스터마이징 로직
    private OAuth2AuthorizationRequest customizeRequest(OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) return null; // 요청이 null인 경우 그대로 반환

        // 기존 추가 파라미터 복사
        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

        // registrationId 추출( ex: "google", "naver")
        String registrationId =(String) authorizationRequest.getAttributes().get(OAuth2ParameterNames.REGISTRATION_ID);

        // provider에 따라 prompt 파라미터를 다르게 설정
        if ("google".equals(registrationId)) {
            // Google: 이전 계정 로그인 기록 무시하고 계정 선택 화면 강제
            additionalParameters.put("prompt", "select_account");
        } else if ("naver".equals(registrationId)) {
            /**
             *  Naver: 자동 로그인 유지
             * - 네이버는 구글처럼 계정 선택 기능(select_account)을 제공하지 않으며,
             *   로그인 화면을 강제로 띄우는 옵션(auth_type=reauthenticate)만 지원한다.
             * - 그러나 강제 로그인 화면은 사용자 경험(UX)을 해칠 수 있으므로,
             *   본 프로젝트에서는 네이버 로그인 시 자동 로그인을 유지하기로 결정했다.
             * - 따라서 additionalParameters에는 아무 값도 넣지 않아 빈 Map을 전달한다.
             */
            // additionalParameters.put("auth_type", "reauthenticate");
        }

        // 기존 요청을 기반으로 새로운 AuthorizationRequest 생성 (추가 파라미터 반영)
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }
}