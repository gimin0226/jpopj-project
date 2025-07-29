package com.gimin.jpopblog.consumer.web;

import com.gimin.jpopblog.domain.comment.dto.CommentResponseDto;
import com.gimin.jpopblog.domain.comment.service.CommentService;
import com.gimin.jpopblog.global.config.auth.dto.SessionUser;
import com.gimin.jpopblog.domain.post.entity.TagType;
import com.gimin.jpopblog.domain.post.service.PostsService;
import com.gimin.jpopblog.domain.post.dto.PostCreateRequestDto;
import com.gimin.jpopblog.domain.post.dto.PostResponseDto;
import com.gimin.jpopblog.domain.post.dto.PostSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {


    private final PostsService postsService;
    private final CommentService commentService;

    @GetMapping(value = {"/", "/{category}"})
    public String blogPage(@PathVariable(required = false) String category, Model model) {
        category= (category==null)?"NEW":category.toUpperCase();
        System.out.println(category);
        TagType activeCategory = TagType.from(category);

        List<PostSummaryDto> posts = postsService.findByCategory(activeCategory);

        model.addAttribute("posts", posts);
        model.addAttribute("activeCategory",category);

        return "index";
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> noFavicon(){
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public String userProfile(Model model, @SessionAttribute("user") SessionUser user){
        model.addAttribute("user",user);
        return "profile";
    }
    @GetMapping(value = {"/write"})
    public String blogWritePage(Model model){
        model.addAttribute("post",new PostCreateRequestDto());
        return "write";
    }

    @GetMapping("/login")
    public String blogLoginPage(Model model){
        return "login";
    }

    @GetMapping("/posts/{postId}")
    public String viewPostDetail(@PathVariable Long postId, Model model){
        PostResponseDto dto = postsService.findById(postId);
        List<CommentResponseDto> comments = commentService.findByPostIdOrderedByCreatedDateAsc(postId);
        model.addAttribute("post",dto);
        model.addAttribute("comments",comments);
        System.out.println("게시글 상세 페이지 controller");
        return "post-detail";
    }
}
