package com.gimin.jpopblog.domain.post.entity;

import java.util.Arrays;

public enum TagType {
    NEW,
    ARTIST,
    REVIEW,
    TALK,
    CONCERT;

    public static TagType from(String category){
        return Arrays.stream(values())
                .filter(tag-> tag.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 태그입니다."));
    }
}
