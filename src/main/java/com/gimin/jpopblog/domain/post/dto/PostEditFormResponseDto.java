package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;

public record PostEditFormResponseDto(Long postId, String title, String content, TagType category){
    public static PostEditFormResponseDto from(Post p){
        return new PostEditFormResponseDto(p.getPostId(),p.getTitle(),p.getContent(),p.getCategory());
    }
}