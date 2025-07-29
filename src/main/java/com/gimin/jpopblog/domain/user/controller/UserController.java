package com.gimin.jpopblog.domain.user.controller;


import com.gimin.jpopblog.domain.user.dto.AdditionalInfoRequestDto;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.service.UserService;
import com.gimin.jpopblog.global.config.auth.dto.OAuthAttributes;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;
    @GetMapping("/users/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNicknameDuplicate(@RequestParam String nickname){
        Map<String,Boolean> response = new HashMap<>();
        System.out.println(nickname);
        response.put("isDuplicate", userService.isNicknameDuplicate(nickname));
        return response;
    }

    @GetMapping("/signup-more-info")
    public String signupMoreInfo(Model model){
        model.addAttribute("requestDto", new AdditionalInfoRequestDto());
        return "signup-more-info";
    }

    @PostMapping("/signup/complete")
    public String completeSignUp(Model model, @ModelAttribute AdditionalInfoRequestDto requestDto){
        // 1. 세션에서 소셜 로그인 정보 가져오기

        OAuthAttributes socialAttributes = (OAuthAttributes) httpSession.getAttribute("social_info");
        if(socialAttributes==null){
            throw new IllegalArgumentException("세션 정보가 없습니다.");
        }

        // 2. 서비스에 정보들을 넘겨 최종 회원가입 처리
        User newUser = userService.completeSignUp(socialAttributes, requestDto);

        // 3. 기존 세션 정보 삭제 및 새 정보로 업데이트
        httpSession.removeAttribute("social_info");
        httpSession.setAttribute("user",new SessionUser(newUser));

        // 4. Spring Security의 현재 인증 정보 업데이트
        updateAuthentication(socialAttributes,newUser);
        System.out.println(requestDto);
        return "redirect:/";
    }


    public void updateAuthentication(OAuthAttributes socialAttributes, User user) {

        /*
         * 이 메서드는 추가 정보 기입 등으로 사용자의 정보(특히 권한)가 변경되었을 때,
         * 현재 세션에 반영된 Spring Security의 인증 정보를 수동으로 갱신하는 역할을 한다.
         * 이를 통해 사용자는 로그아웃 후 다시 로그인할 필요 없이 즉시 새로운 권한을 적용받게 된다.
         */

        // 사용자의 최종 정보(provider, role 등)와 기존 소셜 로그인 속성을 바탕으로
        // 새로운 OAuthAttributes 객체를 생성한다.
        OAuthAttributes attributes = OAuthAttributes.of(user.getProvider(),
                socialAttributes.getNameAttributeKey(),
                socialAttributes.getAttributes());

        // 새로운 인증 정보를 담을 Principal 객체(DefaultOAuth2User)를 생성한다.
        DefaultOAuth2User newOAuth2User = new DefaultOAuth2User(
                // 1. 가장 중요한 부분: DB에 저장된 사용자의 최종 권한(Role)을 가져와 설정한다.
                //    (예: ROLE_GUEST -> ROLE_USER)
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                // 2. 소셜 로그인 시 받아온 사용자의 속성 맵을 그대로 사용한다.
                attributes.getAttributes(),
                // 3. 사용자 이름의 기준이 되는 속성의 키 값을 설정한다.
                attributes.getNameAttributeKey()
        );

        // 새로운 인증(Authentication) 객체, 즉 '인증 티켓'을 생성한다.
        // UsernamePasswordAuthenticationToken은 비밀번호 방식 외에도 범용적으로 사용된다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                newOAuth2User, // Principal (인증된 사용자 주체)
                null,          // Credentials (자격 증명, 보통 null로 처리)
                newOAuth2User.getAuthorities() // Authorities (갱신된 사용자 권한)
        );

        // SecurityContextHolder에 있는 현재 SecurityContext에 새로운 인증 정보를 설정한다.
        // 이 코드를 통해 현재 세션의 인증 정보가 실시간으로 갱신된다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

//updateAuthentication 메서드 상세 설명
/*
네, 반드시 둘 다 해야 합니다.

두 코드는 완전히 다른 목적을 가지고 있으며, 하나만으로는 원하는 결과를 얻을 수 없습니다.

httpSession.setAttribute(...): 애플리케이션이 사용하기 위한 단순 정보 저장.

SecurityContextHolder...setAuthentication(...): Spring Security가 사용하기 위한 공식적인 인증/권한 정보 업데이트.

### httpSession.setAttribute(...) - 애플리케이션용 세션 정보
이 코드는 HttpSession에 "user"라는 이름으로 SessionUser 객체를 저장하는, 우리 애플리케이션만의 규칙입니다.

목적: 컨트롤러나 뷰(HTML)에서 로그인한 사용자의 이름이나 이메일을 간단히 꺼내 화면에 보여주는 등, 애플리케이션의 편의를 위해 사용합니다.

한계: Spring Security는 "user"라는 키에 무엇이 들었는지 전혀 알지 못합니다. 따라서 이 값만 바꾼다고 해서 사용자의 권한이 ROLE_USER로 바뀌거나, 권한이 필요한 페이지에 접근할 수 있게 되는 것이 아닙니다.

### SecurityContextHolder...setAuthentication(...) - Spring Security용 인증/권한 정보
이 코드는 Spring Security의 공식적인 인증 시스템에 직접 개입하여 사용자의 정보를 갱신하는 것입니다.

목적: Spring Security가 "이 사용자는 누구이며, 어떤 권한(ROLE_USER, ROLE_ADMIN 등)을 가지고 있는가?"를 판단하는 공식적인 데이터를 업데이트합니다.

중요성: Spring Security의 필터들은 URL 접근 제어(hasRole('USER'))나 메서드 보안 등을 처리할 때, HttpSession의 다른 값은 전혀 보지 않고 오직 SecurityContextHolder에 있는 인증 정보만 확인합니다. 따라서 사용자의 권한을 GUEST에서 USER로 실제로 "업그레이드"하려면 이 코드가 반드시 필요합니다.

## 요약 및 비유 analogy
두 작업은 마치 행사장에서 이름표와 출입증을 따로 받는 것과 같습니다.

httpSession.setAttribute("user", ...): 다른 사람과 대화하기 편하도록 가슴에 다는 이름표와 같습니다. (애플리케이션의 편의 기능)

SecurityContextHolder.setAuthentication(...): 특정 구역에 들어갈 수 있는지 보안요원이 확인하는 **공식 출입증(Access Card)**과 같습니다. (Spring Security의 권한 관리)

이름표만 바꾼다고 해서 VIP 라운지에 들어갈 수 없듯이, HttpSession의 값만 바꿔서는 권한이 필요한 페이지에 접근할 수 없습니다. 반드시 공식 출입증인 Authentication 객체를 갱신해주어야 합니다.
 */