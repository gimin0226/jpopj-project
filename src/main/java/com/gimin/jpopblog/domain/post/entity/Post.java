package com.gimin.jpopblog.domain.post.entity;

import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long postId;

    // 외부 애그리거트(User)는 ID로만 소유
    @Column(name="user_id",nullable = false)
    private Long userId;

    //조회 전용, DB 칼럼 추가되거나 수정 없음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", insertable = false, updatable = false)
    @ToString.Exclude  //Lombok이 @ToString을 통해 자동 생성하는 toString()에서 해당 필드를 제외함
    private User user;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TagType category;

    @Builder
    public Post(String title, String content, User user, TagType category) {
        this.title = title;
        this.content = content;
        this.user= user;
        this.userId= user.getId();
        this.category = category;
    }

    public void update(String title, String content, TagType category){
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
