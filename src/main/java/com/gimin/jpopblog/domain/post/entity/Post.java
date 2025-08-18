package com.gimin.jpopblog.domain.post.entity;

import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TagType category;

    @Builder
    public Post(String title, String content, User user, TagType category) {
        this.title = title;
        this.content = content;
        this.user= user;
        this.category = category;
    }

    public void update(String title, String content, TagType category){
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
