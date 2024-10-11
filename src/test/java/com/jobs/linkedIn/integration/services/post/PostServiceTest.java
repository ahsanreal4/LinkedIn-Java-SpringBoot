package com.jobs.linkedIn.integration.services.post;

import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.post.PostService;
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

import java.util.List;

@SpringBootTest
public class PostServiceTest {


    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserUtils userUtils;

    User user;
    User user2;

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

        userRepository.save(user);
        userRepository.save(user2);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    CreatePostDto getNewCreatePostDto() {
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTitle("some title");
        createPostDto.setDescription("some description");

        return createPostDto;
    }

    @Test
    void SHOULD_REGSITER_POST() {
        CreatePostDto createPostDto = getNewCreatePostDto();

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertInstanceOf(PostDto.class, this.postService.createPost(createPostDto));
    }

    @Test
    void SHOULD_NOT_REGSITER_POST_WHEN_USER_NOT_EXIST() {
        CreatePostDto createPostDto = getNewCreatePostDto();

        Mockito.when(userUtils.getEmail()).thenReturn("rafasfsafsa");

        Assertions.assertThrows(ApiException.class, () -> {
            this.postService.createPost(createPostDto);
        });
    }

    @Test
    void SHOULD_RETURN_POST_BY_ID() {
        CreatePostDto createPostDto = getNewCreatePostDto();

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        PostDto postDto = this.postService.createPost(createPostDto);
        Assertions.assertInstanceOf(PostDto.class, this.postService.getPostById(postDto.getId()));
    }

    @Test
    void SHOULD_NOT_RETURN_POST_BY_ID() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.postService.getPostById(1L);
        });
    }

    @Test
    void SHOULD_RETURN_ALL_POSTS() {
        CreatePostDto createPostDto = getNewCreatePostDto();
        CreatePostDto createPostDto1 = getNewCreatePostDto();
        createPostDto1.setTitle("something else");

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        this.postService.createPost(createPostDto);
        this.postService.createPost(createPostDto1);

        List<PostDto> posts = this.postService.getAllPosts();

        Assertions.assertEquals(2, posts.size());
    }

    @Test
    void SHOULD_DELETE_POST_BY_ID() {
        CreatePostDto createPostDto = getNewCreatePostDto();

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        PostDto postDto = this.postService.createPost(createPostDto);

        Assertions.assertInstanceOf(String.class, this.postService.deletePostById(postDto.getId()));
    }

    @Test
    void SHOULD_NOT_DELETE_POST_BY_ID_WHEN_USER_NOT_EXIST() {
        Mockito.when(userUtils.getEmail()).thenReturn("31r13r31");

        Assertions.assertThrows(ApiException.class, () -> {
            this.postService.deletePostById(9999L);
        });
    }

    @Test
    void SHOULD_NOT_DELETE_POST_BY_ID_WHEN_POST_NOT_FOUND() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.postService.deletePostById(9999L);
        });
    }

}
