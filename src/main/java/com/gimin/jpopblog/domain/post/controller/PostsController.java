package com.gimin.jpopblog.domain.post.controller;

import com.gimin.jpopblog.domain.comment.dto.CommentResponseDto;
import com.gimin.jpopblog.domain.comment.service.CommentService;
import com.gimin.jpopblog.domain.post.dto.PostEditFormResponseDto;
import com.gimin.jpopblog.domain.post.dto.PostResponseDto;
import com.gimin.jpopblog.domain.post.service.PostsService;
import com.gimin.jpopblog.domain.post.dto.PostCreateRequestDto;
import com.gimin.jpopblog.domain.post.dto.PostUpdateRequestDto;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;
    private final CommentService commentService;

    @PostMapping
    public String save(@ModelAttribute PostCreateRequestDto requestDto, HttpSession session){
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
       postsService.save(requestDto, sessionUser);
       return "redirect:/"+requestDto.getCategory();
    }

    @GetMapping("/write")
    public String writeForm(Model model){
        model.addAttribute("mode", "create");
        model.addAttribute("post",new PostCreateRequestDto());
        return "write";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @SessionAttribute("user") SessionUser sessionUser, Model model){
        PostEditFormResponseDto post = postsService.findForEdit(id, sessionUser.getId());
        model.addAttribute("mode","edit");
        model.addAttribute("post",post);
        return "write";
    }

    @GetMapping("/{postId}")
    public String viewPostDetail(@PathVariable Long postId, Model model){
        PostResponseDto dto = postsService.findById(postId);
        List<CommentResponseDto> comments = commentService.findByPostIdOrderedByCreatedDateAsc(postId);
        model.addAttribute("post",dto);
        model.addAttribute("comments",comments);
        System.out.println("게시글 상세 페이지 controller");
        return "post-detail";
    }

    @PatchMapping("/{postId}")
    public String update(@PathVariable Long postId, @ModelAttribute PostUpdateRequestDto requestDto){
        postsService.update(postId, requestDto);
        return "redirect:/posts/"+ postId;
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @SessionAttribute("user") SessionUser sessionUser){
        System.out.println("PostController의 delete()메서드 호출");
        postsService.delete(postId,sessionUser.getId());
        return ResponseEntity.noContent().build();
    }


}
