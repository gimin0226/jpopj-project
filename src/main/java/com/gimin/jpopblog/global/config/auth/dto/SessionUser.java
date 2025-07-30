package com.gimin.jpopblog.global.config.auth.dto;

import com.gimin.jpopblog.domain.user.entity.Nickname;
import com.gimin.jpopblog.domain.user.entity.Role;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
public class  SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String nickname;
    private Role role;
    private String registrationId;

    //정회원일 때 생성자
    public SessionUser(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.nickname = user.getNickname().getValue();
        this.role = Role.USER;
        this.registrationId = user.getProvider();
    }

    //가회원일 때 생성자
    public SessionUser(OAuthAttributes attributes){
        this.id= null;
        this.name = attributes.getName();
        this.email = attributes.getEmail();
        this.picture = attributes.getPicture();
        this.nickname = null;
        this.role = Role.GUEST;
        this.registrationId= attributes.getRegistrationId();
    }

    //가회원일 때 추가 정보 입력받아 User 객체 만들기
    public User toEntity(Nickname nickname, LocalDate birthDate){
        return User.builder()
                .email(this.email)
                .name(this.name)
                .picture(this.picture)
                .role(Role.USER)
                .nickname(nickname)
                .birthDate(birthDate)
                .provider(this.registrationId)
                .build();
    }
}