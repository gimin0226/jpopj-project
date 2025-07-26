package com.gimin.jpopblog.domain.comment.dto;

import com.gimin.jpopblog.domain.comment.entity.Comment;
import com.gimin.jpopblog.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private Long postId;
    private String userName;
    private Long commentId;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public CommentResponseDto(Long commentId, Long postId, String author, String content, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static CommentResponseDto of(Comment comment, String userName){
        CommentResponseDto dto = new CommentResponseDto();
        dto.postId = comment.getPostId();
        dto.userName = userName;
        dto.commentId = comment.getCommentId();
        dto.content = comment.getContent();
        dto.createdDate = comment.getCreatedDate();

        return dto;
    }

}
