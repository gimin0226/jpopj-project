package com.gimin.jpopblog.domain.user.service;

import com.gimin.jpopblog.domain.user.dto.AdditionalInfoRequestDto;
import com.gimin.jpopblog.domain.user.entity.Nickname;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.repository.UserRepository;
import com.gimin.jpopblog.global.config.auth.dto.OAuthAttributes;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User completeSignUp(SessionUser sessionUser, AdditionalInfoRequestDto requestDto){
        //소셜 정보와 추가 정보를 합쳐서 User 엔티티 생성 후 db 저장(그 전에는 세션만 저장하고 db에 저장 안함)
        User user = sessionUser.toEntity(new Nickname(requestDto.getNickname()), requestDto.getBirthDate());
        return userRepository.save(user);
    }


    @Transactional
    public void changeNickname(Long userId, String nickname){

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 변경하려는 닉네임이 현재 닉네임과 같다면,
        // 닉네임 중복 검사
        userRepository.findByNicknameValue(nickname).ifPresent((nick)->{
            throw new IllegalArgumentException(nickname + "은 이미 존재하는 닉네임입니다.");
        });

        //닉네임은 내부 상태를 변경하는 것이 아니라 새로운 값 객체를 만들어서 전달
        Nickname newNickname = new Nickname(nickname);

        //조회한 사용자의 닉네임 변경 (더티 체킹으로 DB에 반영됨)
        user.changeNickname(newNickname);
    }

    //닉네임 중복 여부만 확인하는 메서드(실시간으로 닉네임이 중복되는지 프론트에게 중복여부만 리턴)
    public boolean isNicknameDuplicate(String nickname){
        return userRepository.existsByNicknameValue(nickname);
    }

}
