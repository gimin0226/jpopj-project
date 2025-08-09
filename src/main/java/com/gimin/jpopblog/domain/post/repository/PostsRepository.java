package com.gimin.jpopblog.domain.post.repository;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    //카테고리에 해당하는 게시글 목록이 최신순으로 정렬되도록 
    List<Post> findByCategoryOrderByCreatedDateDesc(TagType category);
}
