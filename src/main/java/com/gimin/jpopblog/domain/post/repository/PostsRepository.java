package com.gimin.jpopblog.domain.post.repository;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(TagType category);
}
