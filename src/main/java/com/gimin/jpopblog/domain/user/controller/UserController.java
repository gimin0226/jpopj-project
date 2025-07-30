package com.gimin.jpopblog.domain.user.controller;


import com.gimin.jpopblog.domain.user.dto.AdditionalInfoRequestDto;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.service.UserService;
import com.gimin.jpopblog.global.config.auth.LoginUser;
import com.gimin.jpopblog.global.config.auth.dto.OAuthAttributes;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/users/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        Map<String, Boolean> response = new HashMap<>();
        System.out.println(nickname);
        response.put("isDuplicate", userService.isNicknameDuplicate(nickname));
        return response;
    }

    @GetMapping("/signup-more-info")
    public String signupMoreInfo(Model model) {
        model.addAttribute("requestDto", new AdditionalInfoRequestDto());
        return "signup-more-info";
    }

    @PostMapping("/signup/complete")
    public String completeSignUp(@LoginUser("social_info") SessionUser sessionUser, @ModelAttribute AdditionalInfoRequestDto requestDto) {
        // 1. 세션에서 소셜 로그인 정보 가져오기
        if (sessionUser == null) {
            throw new IllegalArgumentException("세션 정보가 없습니다.");
        }

        System.out.println(sessionUser);

        // 2. 서비스에 정보들을 넘겨 최종 회원가입 처리
        User newUser = userService.completeSignUp(sessionUser, requestDto);

        // 3. 기존 세션 정보 삭제 및 새 정보로 업데이트
        httpSession.removeAttribute("social_info");
        httpSession.setAttribute("user", new SessionUser(newUser));

        // 4. Spring Security의 현재 인증 정보 업데이트
        updateAuthentication(newUser);
        return "redirect:/";
    }


    // 더 이상 OAuthAttributes가 필요 없으므로 파라미터를 User만 받도록 변경합니다.
    public void updateAuthentication(User user) {
        // 1. 현재 SecurityContext에서 인증 정보(Authentication) 가져오기
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // 2. 새로운 권한 생성
        // 사용자의 최종 권한(e.g., ROLE_USER)으로 새로운 Authority 리스트를 만듭니다.
        List<GrantedAuthority> newAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRoleKey()));

        // 3. 새 인증 정보 생성
        // 기존 Principal 객체와 새로운 권한으로 새 Authentication 객체를 생성합니다.
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                currentAuth.getPrincipal(), // 기존 Principal 사용
                currentAuth.getCredentials(), // 기존 Credentials 사용
                newAuthorities // 새로 만든 권한 사용
        );

        // 4. SecurityContext에 새 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
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