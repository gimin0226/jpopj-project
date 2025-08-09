package com.gimin.jpopblog.global.config.auth.config;

import com.gimin.jpopblog.global.config.auth.CustomAuthorizationRequestResolver;
import com.gimin.jpopblog.global.config.auth.handler.OAuth2LoginSuccessHandler;
import com.gimin.jpopblog.global.config.auth.service.CustomOAuth2UserService;
import com.gimin.jpopblog.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    //사용자 정보를 처리할 커스텀 서비스
    private final CustomOAuth2UserService customOAuth2UserService;
    //로그인 성공 후 로직 처리 핸들러
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    //OAuth2 클라이언트 등록 정보를 관리
    private final ClientRegistrationRepository clientRegistrationRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        // 1. 커스텀 AuthorizationRequestResolver 생성(prompt=login/select_account 설정용)
        OAuth2AuthorizationRequestResolver customResolver = new CustomAuthorizationRequestResolver(clientRegistrationRepository,"/oauth2/authorization");


        httpSecurity
                // 2. CSRF 보안 기능 비활성화 ( API 서버/테스트에선 보통 비활성화함)
                .csrf(csrf->csrf.disable())
                // 3. H2 콘솔 접속 오류 방지를 위해 X-Frame-Options 비활성화
                .headers(headers->headers.frameOptions().disable())
                // 4.요청별 접근 권한 설정
                .authorizeHttpRequests(auth->auth
                        // 정적 리소스 및 공개 페이지는 모두 허용
                        .requestMatchers("/","/css/**","/images/**","/js/**","/h2-console/**","/NEW","/ARTIST","/REVIEW","/TALK","/CONCERT").permitAll()
                        // "/api/v1/**" 경로는 USER 권한 이상만 접근 가능
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) //.name()은 "USER" 리턴 -> hasRole에는 "ROLE_"없이 넣지만 내부적으로 "ROLE_"이 붙어서 비교됨
                        //그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 5. 로그아웃 설정
                .logout(logout->logout
                        .logoutSuccessUrl("/")  //로그아웃 성공 시 이동할 URL
                        .invalidateHttpSession(true)   //세션 무효화
                )
                // 6. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        //6-1. 인증 요청 시 사용자 정의 Resolver 사용 (prompt 옵션 삽입용)
                        .authorizationEndpoint(authEndpoint->authEndpoint
                                .authorizationRequestResolver(customResolver))
                        // 6-2. 로그인 성공 후 사용자 정보 처리
                        .userInfoEndpoint(userInfo->userInfo
                                .userService(customOAuth2UserService))    //1. 유저 정보 가져올 때 쓸 서비스 등록
                        // 6-3. 로그인 성공 후 후처리 핸들러 등록
                        .successHandler(oAuth2LoginSuccessHandler)
                );
        // 최종적으로 SecurityFilterChain 반환 (보안 필터 체인 설정 완료)
        return httpSecurity.build();
    }
}