package com.gimin.jpopblog.domain.comment.controller;

import com.gimin.jpopblog.domain.comment.dto.CommentCreateRequestDto;
import com.gimin.jpopblog.domain.comment.service.CommentService;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public String createComment(@PathVariable Long postId, @Valid @ModelAttribute CommentCreateRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "redirect:/posts/"+postId+"?error";
        }
        System.out.println("댓글 추가 controller");
        commentService.create(requestDto);
        return "redirect:/posts/"+postId;
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @SessionAttribute("user") SessionUser sessionUser){
        System.out.println("aaaaa");
        commentService.delete(commentId, sessionUser.getId());
        return ResponseEntity.noContent().build();  //204,리다이렉트 없음
    }
}
