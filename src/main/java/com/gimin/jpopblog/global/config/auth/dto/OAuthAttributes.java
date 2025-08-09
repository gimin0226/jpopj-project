package com.gimin.jpopblog.global.config.auth.dto;

import com.gimin.jpopblog.domain.user.entity.Nickname;
import com.gimin.jpopblog.global.config.auth.dto.impl.GoogleUserInfo;
import com.gimin.jpopblog.global.config.auth.dto.impl.KakaoUserInfo;
import com.gimin.jpopblog.global.config.auth.dto.impl.NaverUserInfo;
import com.gimin.jpopblog.domain.user.entity.Role;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {
    //Object는 어떤 타입이든 저장 가능
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final String registrationId;  //provider 정보

    @Builder
    public OAuthAttributes(OAuthUserInfo userInfo, String nameAttributeKey, String registrationId){
        this.attributes= userInfo.getAttributes();
        this.nameAttributeKey = nameAttributeKey;
        this.name= userInfo.getName();
        this.email = userInfo.getEmail();
        this.picture = userInfo.getPicture();
        this.registrationId = registrationId;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeKey, Map<String, Object> attributes){

        OAuthProvider provider = OAuthProvider.from(registrationId);

        OAuthUserInfo userInfo = switch(provider){
            case GOOGLE -> new GoogleUserInfo(attributes);
            case NAVER -> new  NaverUserInfo(attributes);
            case KAKAO -> new KakaoUserInfo(attributes);
        };

        return new OAuthAttributes(userInfo, userNameAttributeKey, registrationId);
    }







 }
