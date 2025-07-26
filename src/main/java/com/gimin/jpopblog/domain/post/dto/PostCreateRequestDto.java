package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
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
    private String userName;
    private TagType category;

    @Builder
    public PostCreateRequestDto(String title, String content, String userName, TagType category) {
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.category = category;
    }

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .author(userName)
                .category(category)
                .build();
    }
}
