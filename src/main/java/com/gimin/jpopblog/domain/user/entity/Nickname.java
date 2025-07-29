package com.gimin.jpopblog.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Nickname {
    @Column(name="nickname", nullable = false, length = 20)
    private String value;

    public Nickname(String value){
        if(value==null||value.isBlank()){
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }

        if(value.length()>20){
            throw new IllegalArgumentException("닉네임은 20자 이하여야 합니다.");
        }
        this.value= value;
    }
}
