package com.jobs.linkedIn.integration.services.post;

import com.jobs.linkedIn.dto.post.comment.CreatePostCommentDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.post.CommentService;
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
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

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

    CreatePostCommentDto getCreatePostCommentDto(long postId) {
        CreatePostCommentDto createPostCommentDto = new CreatePostCommentDto();
        createPostCommentDto.setPostId(postId);
        createPostCommentDto.setText("Some text");

        return createPostCommentDto;
    }

    @Test
    void SHOULD_ADD_COMMENTS_TO_POST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.addPostComment(createPostCommentDto));
        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.addPostComment(createPostCommentDto));
    }

    @Test
    void SHOULD_ADD_REPLY_COMMENTS_TO_POST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);
        createPostCommentDto.setParentId(postCommentDto.getId());
        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.addPostComment(createPostCommentDto));
    }

    @Test
    void SHOULD_NOT_ADD_COMMENT_WHEN_POST_NOT_EXIST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(9999L);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_NOT_ADD_COMMENT_WHEN_USER_NOT_EXIST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn("fsafsafsa");

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_NOT_ADD_COMMENT_WHEN_PARENT_COMMENT_NOT_EXIST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        this.commentService.addPostComment(createPostCommentDto);
        createPostCommentDto.setParentId(9999L);

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_RETURN_POST_COMMENTS() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        this.commentService.addPostComment(createPostCommentDto);
        this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertEquals(2, this.commentService.getPostComments(post.getId()).size());
    }

    @Test
    void SHOULD_RETURN_REPLY_COMMENTS() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);
        createPostCommentDto.setParentId(postCommentDto.getId());
        this.commentService.addPostComment(createPostCommentDto);
        this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertEquals(2, this.commentService.getReplyComments(postCommentDto.getId()).size());
    }

    @Test
    void SHOULD_RETURN_COMMENT_BY_ID() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.getCommentById(postCommentDto.getId()));
    }

    @Test
    void SHOULD_NOT_RETURN_COMMENT_BY_ID() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        this.commentService.addPostComment(createPostCommentDto);
        createPostCommentDto.setParentId(9999L);

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_DELETE_COMMENT_BY_ID() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertInstanceOf(String.class, this.commentService.deleteCommentById(postCommentDto.getId()));
    }

    @Test
    void SHOULD_NOT_DELETE_COMMENT_WHEN_USER_NOT_EXIST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);
        Mockito.when(userUtils.getEmail()).thenReturn("fasfsafsa");

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(postCommentDto.getId());
        });
    }

    @Test
    void SHOULD_NOT_DELETE_COMMENT_WHEN_COMMENT_NOT_EXIST() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(99999L);
        });
    }

    @Test
    void SHOULD_NOT_DELETE_ANOTHER_USER_COMMENT() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);
        Mockito.when(userUtils.getEmail()).thenReturn(user2.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(postCommentDto.getId());
        });
    }

    @Test
    void SHOULD_DELETE_ANOTHER_USER_COMMENT_IF_ADMIN() {
        CreatePostCommentDto createPostCommentDto = getCreatePostCommentDto(post.getId());

        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(true);
        Mockito.when(userUtils.getEmail()).thenReturn(user2.getEmail());
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);

        Assertions.assertInstanceOf(String.class, this.commentService.deleteCommentById(postCommentDto.getId()));
    }

}
