package com.gimin.jpopblog.domain.comment.controller;

import com.gimin.jpopblog.domain.comment.dto.CommentCreateRequestDto;
import com.gimin.jpopblog.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable String postId, @Valid @ModelAttribute CommentCreateRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "redirect:/posts/"+postId+"?error";
        }
        System.out.println("댓글 추가 controller");
        commentService.create(requestDto);
        return "redirect:/posts/"+postId;
    }
}
