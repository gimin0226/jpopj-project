package com.gimin.jpopblog.domain.post.entity;

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
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    private TagType category;

    @Builder
    public Post(String title, String content, String author, TagType category) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
