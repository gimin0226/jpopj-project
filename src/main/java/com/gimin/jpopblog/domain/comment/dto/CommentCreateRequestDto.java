package com.gimin.jpopblog.domain.comment.dto;

import com.gimin.jpopblog.domain.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequestDto {

    @NotNull(message="게시글 ID는 필수입니다.")
    private Long postId;

    @NotNull(message="작성자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message="댓글 내용은 비워둘 수 없습니다.")
    @Size(max = 1000, message="댓글은 1000자 이내로 작성해주세요.")
    private String content;
    //private String likeId;

    @Builder
    public CommentCreateRequestDto(Long postId, Long userId, String content){
        this.postId= postId;
        this.userId = userId;
        this.content = content;
    }

    public Comment toEntity(){
        return Comment.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
    }
}
