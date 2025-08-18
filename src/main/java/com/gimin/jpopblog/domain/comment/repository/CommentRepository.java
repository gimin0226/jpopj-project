package com.gimin.jpopblog.domain.comment.repository;

import com.gimin.jpopblog.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostIdOrderByCreatedDateAsc(Long postId);

    @Query("""
        select c from Comment c
        join fetch c.user u
        where c.commentId= :commentId
    """)
    Optional<Comment> findByIdWithUser(@Param("commentId") Long commentId);
}
