package com.gimin.jpopblog.global.config.auth.config;

import com.gimin.jpopblog.global.config.auth.handler.OAuth2LoginSuccessHandler;
import com.gimin.jpopblog.global.config.auth.service.CustomOAuth2UserService;
import com.gimin.jpopblog.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())
                .headers(headers->headers.frameOptions().disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/","/css/**","/images/**","/js/**","/h2-console/**","/NEW","/ARTIST","/REVIEW","/TALK","/CONCERT").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) //.name()은 "USER" 리턴 -> hasRole에는 "ROLE_"없이 넣지만 내부적으로 "ROLE_"이 붙어서 비교됨
                        .anyRequest().authenticated()
                )
                .logout(logout->logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo->userInfo
                                .userService(customOAuth2UserService))    //1. 유저 정보 가져올 때 쓸 서비스 등록
                        .successHandler(oAuth2LoginSuccessHandler)        //2. 로그인 성공 후 처리할 핸들러 등록
                );

        return httpSecurity.build();
    }
}