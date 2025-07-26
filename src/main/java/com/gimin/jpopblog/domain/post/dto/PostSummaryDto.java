package com.gimin.jpopblog.domain.post.dto;

import com.gimin.jpopblog.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDto {
    private String title;
    private String author;
    private LocalDateTime date;
    private Long id;

    public static PostSummaryDto from(Post post){
        PostSummaryDto dto = new PostSummaryDto();
        dto.title= post.getTitle();
        dto.author = post.getAuthor();
        dto.date = post.getCreatedDate();
        dto.id = post.getId();
        return dto;
    }
}
