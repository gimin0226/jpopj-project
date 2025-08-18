package com.gimin.jpopblog.domain.comment.entity;

import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    private Long commentId;

    //Post, User 엔티티 id로 접근, 애그리거트 간 결합 방지
    @Column(name="post_id", nullable = false)
    private Long postId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(length = 1000 , nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user; //조회 전용


    //Like 엔티티 만든 후
    //private Long likeId;

    @Builder
    public Comment(Long postId, Long userId, String content){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
