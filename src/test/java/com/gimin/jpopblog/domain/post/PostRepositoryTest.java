package com.gimin.jpopblog.domain.post;

import com.gimin.jpopblog.domain.post.entity.Post;
import com.gimin.jpopblog.domain.post.repository.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("PostsRepository 테스트")
public class PostRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }
/*
    @Test
    @DisplayName("게시글 저장 후 조회하면 값이 일치해야 한다")
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트 게시물";
        String content = "테스트 본문";

        postsRepository.save(Post.builder()
                .title(title)
                .content(content)
                .author("gimin")
                .build());

        //when
        List<Post> postList = postsRepository.findAll();

        //then
        Post post = postList.get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("userName")
                .build());

        //when
        List<Post> postList = postsRepository.findAll();

        //then
        Post post = postList.get(0);
        System.out.println(">>>>>>>>>>> createDate = "+ post.getCreatedDate() + ", modifiedDate = "+ post.getModifiedDate());

        assertThat(post.getCreatedDate()).isAfter(now);
        assertThat(post.getModifiedDate()).isAfter(now);


    }
    */

}
