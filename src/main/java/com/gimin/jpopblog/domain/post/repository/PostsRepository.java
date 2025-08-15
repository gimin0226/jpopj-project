package com.gimin.jpopblog.domain.post.repository;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Post, Long> {
    //카테고리에 해당하는 게시글 목록이 최신순으로 정렬되도록 
    List<Post> findByCategoryOrderByCreatedDateDesc(TagType category);

    @Query("""
        select p from Post p
        join fetch p.user u
        where p.postId = :postId
    """)
    Optional<Post> findByIdWithUser(@Param("postId") Long postId);
}
