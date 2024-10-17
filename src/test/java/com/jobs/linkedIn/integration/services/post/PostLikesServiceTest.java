package com.jobs.linkedIn.integration.services.post;

import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.interfaces.post.PostLikeService;
import com.jobs.linkedIn.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.Date;

@SpringBootTest
public class PostLikesServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserUtils userUtils;

    User user;
    User user2;
    Post post;

    @BeforeEach
    public void setUp() {
        user = User.builder().
                email("ahsan.btph12355@gmail.com")
                .firstName("Ahsan")
                .lastName("Azeem")
                .password("axeem0099")
                .username("ahsanreal455")
                .build();

        user2 = User.builder()
                .email("ahsan.btph12344@gmail.com")
                .firstName("Ahsan")
                .lastName("Azeem")
                .password("axeem0099")
                .username("ahsanreal45")
                .build();

        post = getNewPost(user);

        userRepository.save(user);
        userRepository.save(user2);
        postRepository.save(post);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    Post getNewPost(User user) {
        Post post1 = new Post();
        post1.setTitle("some post tile");
        post1.setDescription("some post description");
        post1.setPostedBy(user);
        post1.setPostedAt(new Date());

        return post1;
    }

    @Test
    void SHOULD_LIKE_UNLIKE_POST() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
    }

    @Test
    void MULTIPLE_USERS_SHOULD_BE_ABLE_TO_LIKE_POST() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
        Mockito.when(userUtils.getEmail()).thenReturn(user2.getEmail());
        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
    }

    @Test
    void SHOULD_NOT_LIKE_WHEN_POST_NOT_EXIST() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.postLikeService.likePost(9999999L);
        });
    }

    @Test
    void SHOULD_RETURN_POST_LIKES() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        this.postLikeService.likePost(post.getId());
        Mockito.when(userUtils.getEmail()).thenReturn(user2.getEmail());
        this.postLikeService.likePost(post.getId());

        Assertions.assertEquals(2, this.postLikeService.getPostLikes(post.getId()).size());
    }

    @Test
    void SHOULD_RETURN_IS_LIKED_BY_USER() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        this.postLikeService.likePost(post.getId());
        Assertions.assertTrue(this.postLikeService.isLikedByUser(post.getId()));
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        this.postLikeService.likePost(post.getId());
        Assertions.assertFalse(this.postLikeService.isLikedByUser(post.getId()));
    }
}
