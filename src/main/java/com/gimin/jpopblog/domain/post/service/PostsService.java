package com.gimin.jpopblog.domain.post.service;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.entity.TagType;
import com.gimin.jpopblog.domain.post.repository.PostsRepository;

import com.gimin.jpopblog.domain.post.dto.PostResponseDto;
import com.gimin.jpopblog.domain.post.dto.PostCreateRequestDto;
import com.gimin.jpopblog.domain.post.dto.PostSummaryDto;
import com.gimin.jpopblog.domain.post.dto.PostUpdateRequestDto;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    @Transactional
    public Long save(PostCreateRequestDto requestDto, SessionUser sessionUser){
        requestDto.setUserName(sessionUser.getName());
        if(requestDto.getUserName()==null){
            throw new IllegalArgumentException("작성자가 누락되었습니다.");
        }
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){
        Post article = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id: "+ id));

        article.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostResponseDto findById(Long id){
        Post article = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다 id:"+id));
        return PostResponseDto.from(article);
    }

    public List<PostSummaryDto> findByCategory(TagType category){
        return postsRepository.findByCategory(category).stream()
                .map(PostSummaryDto::from)
                .toList();
    }
}
