package com.gimin.jpopblog.global.config.auth.dto;

import com.gimin.jpopblog.global.config.auth.dto.impl.GoogleUserInfo;
import com.gimin.jpopblog.global.config.auth.dto.impl.KakaoUserInfo;
import com.gimin.jpopblog.global.config.auth.dto.impl.NaverUserInfo;
import com.gimin.jpopblog.domain.user.entity.Role;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    //Object는 어떤 타입이든 저장 가능
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuthAttributes(OAuthUserInfo userInfo, String nameAttributeKey){
        this.attributes= userInfo.getAttributes();
        this.nameAttributeKey = nameAttributeKey;
        this.name= userInfo.getName();
        this.email = userInfo.getEmail();
        this.picture = userInfo.getPicture();
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){

        OAuthProvider provider = OAuthProvider.from(registrationId);

        OAuthUserInfo userInfo = switch(provider){
            case GOOGLE -> new GoogleUserInfo(attributes);
            case NAVER -> new NaverUserInfo(attributes);
            case KAKAO -> new KakaoUserInfo(attributes);
        };

        return new OAuthAttributes(userInfo, userNameAttributeName);
    }



    public User toEntity(){
        return User.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .role(Role.GUEST)  //최초 가입자는 GUEST
                .build();
    }


 }
