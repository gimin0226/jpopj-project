package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private TagType category;
    private LocalDateTime createdDate;

    public static PostResponseDto from(Post post){
        PostResponseDto dto = new PostResponseDto();
        dto.id= post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.author = post.getAuthor();
        dto.createdDate = post.getCreatedDate();
        dto.category = post.getCategory();
        return dto;
    }
}
