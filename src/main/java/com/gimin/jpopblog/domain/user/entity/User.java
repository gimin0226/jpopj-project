package com.gimin.jpopblog.domain.user.entity;

import com.gimin.jpopblog.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Nickname nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String provider; //ex: "google", "naver"

    @Builder
    private User(String name, Nickname nickname, LocalDate birthDate, String email, String picture, Role role,String provider){
        this.name = name;
        this.nickname = nickname;
        this.birthDate= birthDate;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
    }

    public User changeNickname(Nickname nickname){
        this.nickname = nickname;
        return this;
    }

    // 소셜 로그인 시 프로필 업데이트를 위한 메소드
    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }

    //추가 정보 기입 후 닉네임, 생년월일 등을 업데이트하는 메소드
    public User completeSignUp(Nickname nickname, LocalDate birthDate){
        this.nickname = nickname;
        this.birthDate = birthDate;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
