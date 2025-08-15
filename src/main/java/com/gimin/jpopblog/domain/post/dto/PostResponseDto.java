package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import com.gimin.jpopblog.domain.user.entity.Nickname;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String userNickname;
    private TagType category;
    private LocalDateTime createdDate;
    private Long userId;

    public static PostResponseDto from(Post post){
        PostResponseDto dto = new PostResponseDto();
        dto.userId = post.getUser().getId();
        dto.postId = post.getPostId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.userNickname = post.getUser().getNickname().getValue();
        dto.createdDate = post.getCreatedDate();
        dto.category = post.getCategory();
        return dto;
    }
}
