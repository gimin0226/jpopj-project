package com.gimin.jpopblog.domain.post.service;

import com.gimin.jpopblog.domain.post.dto.*;
import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import com.gimin.jpopblog.domain.post.exception.PostAccessDeniedException;
import com.gimin.jpopblog.domain.post.exception.PostNotFoundException;
import com.gimin.jpopblog.domain.post.repository.PostsRepository;

import com.gimin.jpopblog.domain.user.entity.User;
import com.gimin.jpopblog.domain.user.repository.UserRepository;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostCreateRequestDto requestDto, SessionUser sessionUser){
        // 1. 사용자 정보가 유효한지 먼저 확인
        if(sessionUser==null){
            throw new IllegalArgumentException("작성자가 누락되었습니다.");
        }

        // 2. SELECT 쿼리 없이 User 프록시 객체 로드
        User userProxy = userRepository.getReferenceById(sessionUser.getId());

        // 3. DTO와 프록시 객체를 사용하여 서비스 계층에서 직접 엔티티 생성
        Post post = requestDto.toEntity(userProxy);

        // 4. 저장 후 ID 반환
        return postsRepository.save(post).getPostId();
    }


    @Transactional(readOnly = true)
    public PostEditFormResponseDto findForEdit(Long postId, Long userId){
        Post post = postsRepository.findByIdWithUser(postId)
                .orElseThrow(()-> new PostNotFoundException(postId));
        if(!post.getUser().getId().equals(userId)){
            throw new PostAccessDeniedException("작성자만 수정할 수 있습니다.");
        }
        return PostEditFormResponseDto.from(post);
    }


    @Transactional
    public Long update(Long postId, PostUpdateRequestDto requestDto){
        Post post = postsRepository.findByIdWithUser(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        System.out.println(requestDto);
        post.update(requestDto.getTitle(),requestDto.getContent(), TagType.from(requestDto.getCategory()));
        return postId;
    }

    public PostResponseDto findById(Long postId){
        Post post = postsRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다 id:"+postId));
        return PostResponseDto.from(post);
    }

    public List<PostSummaryDto> findByCategory(TagType category){
        return postsRepository.findByCategoryOrderByCreatedDateDesc(category).stream()
                .map(PostSummaryDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long postId, Long userId){
        Post post = postsRepository.findByIdWithUser(postId)
                .orElseThrow(()->new PostNotFoundException(postId));
        if(!post.getUser().getId().equals(userId)){
            throw new PostAccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
        postsRepository.delete(post);
        }

}
