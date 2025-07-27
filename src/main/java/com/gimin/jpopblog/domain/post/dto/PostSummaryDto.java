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
    private String userName;
    private LocalDateTime date;
    private Long postId;

    public static PostSummaryDto from(Post post){
        PostSummaryDto dto = new PostSummaryDto();
        dto.title= post.getTitle();
        dto.userName = post.getUser().getName();
        dto.date = post.getCreatedDate();
        dto.postId = post.getPostId();
        return dto;
    }
}
