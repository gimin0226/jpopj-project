package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
    //private User user;
    private TagType category;

    @Builder
    public PostCreateRequestDto(String title, String content, TagType category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post toEntity(User user){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .category(category)
                .build();
    }
}
