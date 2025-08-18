package com.gimin.jpopblog.domain.post.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String category;

    @Builder
    public PostUpdateRequestDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
