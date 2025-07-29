package com.gimin.jpopblog.global.config.auth.dto;

import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class  SessionUser implements Serializable {
    private final Long id;
    private final String name;
    private final String email;
    private final String picture;
    private final String nickname;

    public SessionUser(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.nickname = user.getNickname().getValue();
    }
}