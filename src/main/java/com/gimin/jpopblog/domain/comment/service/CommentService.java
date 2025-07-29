package com.gimin.jpopblog.domain.comment.service;

import com.gimin.jpopblog.domain.comment.dto.CommentCreateRequestDto;
import com.gimin.jpopblog.domain.comment.dto.CommentResponseDto;
import com.gimin.jpopblog.domain.comment.entity.Comment;
import com.gimin.jpopblog.domain.comment.repository.CommentRepository;
import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    @Transactional
    public void create(CommentCreateRequestDto requestDto){
        commentRepository.save(requestDto.toEntity());
    }

    public List<CommentResponseDto> findByPostIdOrderedByCreatedDateAsc(Long postId){

        // 1. 댓글 목록 조회
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedDateAsc(postId);

        for(Comment comment: comments){
            System.out.println("userId"+comment.getUserId());
            System.out.println("contentId"+comment.getContent());
            System.out.println("commentId"+comment.getCommentId());
        }

        // 2. 모든 댓글의 userId 수집
        List<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();

        // 3. userId로 사용자 일괄 조회
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 4. 사용자 이름 포함된 댓글 DTO로 변환
        return comments.stream()
                .map(comment -> {
                    User user = userMap.get(comment.getUserId());
                    if (user == null) {
                        throw new IllegalStateException("댓글 작성자의 유저 정보를 찾을 수 없습니다: userId = " + comment.getUserId());
                    }
                    return CommentResponseDto.of(comment,user.getNickname().getValue());
                })
                .toList();
    }
}
