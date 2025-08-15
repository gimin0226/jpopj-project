package com.gimin.jpopblog.domain.post.controller;

import com.gimin.jpopblog.domain.post.service.PostsService;
import com.gimin.jpopblog.domain.post.dto.PostCreateRequestDto;
import com.gimin.jpopblog.domain.post.dto.PostUpdateRequestDto;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/posts")
    public String save(@ModelAttribute PostCreateRequestDto requestDto, HttpSession session){
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
       postsService.save(requestDto, sessionUser);
       return "redirect:/"+requestDto.getCategory();
    }

    @PutMapping("/posts/{id}")
    public String update(@PathVariable Long id, @ModelAttribute PostUpdateRequestDto requestDto){
        postsService.update(id, requestDto);
        return "redirect:/"+requestDto.getCategory();
    }
    //안녕

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @SessionAttribute("user") SessionUser sessionUser){
        postsService.delete(id,sessionUser);
        return ResponseEntity.noContent().build();
    }
}
