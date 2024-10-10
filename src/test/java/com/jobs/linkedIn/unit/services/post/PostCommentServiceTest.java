package com.jobs.linkedIn.unit.services.post;

import com.jobs.linkedIn.dto.post.comment.CreatePostCommentDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostComment;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostCommentsRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.impl.post.CommentServiceImpl;
import com.jobs.linkedIn.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PostCommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostCommentsRepository postCommentsRepository;

    @Mock
    private UserUtils userUtils;

    User getNewUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("ahsan.bt333@gmail.com");
        user.setUsername("safsafsa");
        user.setPassword("axeem0099");
        user.setFirstName("Ahsan");
        user.setLastName("Azeem");

        return user;
    }

    Post getNewPost(User user) {
        Post post = new Post();
        post.setId(1L);
        post.setPostedBy(user);
        post.setTitle("Some post");
        post.setDescription("Some descirption");

        return post;
    }

    PostComment getNewPostComment(Post post) {
        PostComment postComment = new PostComment();
        postComment.setId(1L);
        postComment.setUser(post.getPostedBy());
        postComment.setPost(post);
        postComment.setText("Some random text");

        return postComment;
    }

    PostCommentDto getNewPostCommentDto(PostComment postComment) {
        PostCommentDto postCommentDto = new PostCommentDto();
        postCommentDto.setPost(postComment.getPost().getId());
        postCommentDto.setUser(postComment.getUser().getId());
        postCommentDto.setText(postComment.getText());
        postCommentDto.setId(postComment.getId());

        return postCommentDto;
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void SHOULD_SAVE_POST_COMMENT() {
        User user = getNewUser();
        Post post = getNewPost(user);
        CreatePostCommentDto createPostCommentDto = new CreatePostCommentDto();
        createPostCommentDto.setPostId(post.getId());
        createPostCommentDto.setText("Some text");
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postCommentsRepository.save(Mockito.any(PostComment.class))).thenReturn(postComment);

        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.addPostComment(createPostCommentDto));
    }

    @Test
    void SHOULD_NOT_SAVE_POST_COMMENT_WHEN_USER_NOT_FOUND() {
        User user = getNewUser();
        Post post = getNewPost(user);
        CreatePostCommentDto createPostCommentDto = new CreatePostCommentDto();
        createPostCommentDto.setPostId(post.getId());
        createPostCommentDto.setText("Some text");

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_NOT_SAVE_POST_COMMENT_WHEN_POST_NOT_FOUND() {
        User user = getNewUser();
        Post post = getNewPost(user);
        CreatePostCommentDto createPostCommentDto = new CreatePostCommentDto();
        createPostCommentDto.setPostId(post.getId());
        createPostCommentDto.setText("Some text");

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }

    @Test
    void SHOULD_NOT_SAVE_POST_COMMENT_WHEN_PARENT_COMMENT_NOT_FOUND() {
        User user = getNewUser();
        Post post = getNewPost(user);
        CreatePostCommentDto createPostCommentDto = new CreatePostCommentDto();
        createPostCommentDto.setPostId(post.getId());
        createPostCommentDto.setText("Some text");
        createPostCommentDto.setParentId(2L);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postCommentsRepository.findById(createPostCommentDto.getParentId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.addPostComment(createPostCommentDto);
        });
    }


    @Test
    void SHOULD_RETURN_COMMENT_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.of(postComment));

        Assertions.assertInstanceOf(PostCommentDto.class, this.commentService.getCommentById(postComment.getId()));
    }

    @Test
    void SHOULD_NOT_RETURN_COMMENT_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.getCommentById(postComment.getId());
        });
    }

    @Test
    void SHOULD_DELETE_COMMENT_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.of(postComment));

        Assertions.assertInstanceOf(String.class, this.commentService.deleteCommentById(postComment.getId()));
    }

    @Test
    void SHOULD_NOT_DELETE_COMMENT_WHEN_USER_NOT_FOUND() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.of(postComment));

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(postComment.getId());
        });
    }

    @Test
    void SHOULD_NOT_DELETE_COMMENT_WHEN_POST_COMMENT_NOT_FOUND() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(postComment.getId());
        });
    }

    @Test
    void SHOULD_NOT_DELETE_SOMEONE_ELSE_COMMENT() {
        User user = getNewUser();
        User user1 = getNewUser();
        user1.setEmail("something else");
        user1.setId(10L);
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user1));
        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.of(postComment));

        Assertions.assertThrows(ApiException.class, () -> {
            this.commentService.deleteCommentById(postComment.getId());
        });
    }

    @Test
    void SHOULD_DELETE_SOMEONE_ELSE_COMMENT_WHEN_ADMIN() {
        User user = getNewUser();
        User user1 = getNewUser();
        user1.setEmail("something else");
        user1.setId(10L);
        Post post = getNewPost(user);
        PostComment postComment = getNewPostComment(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(true);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user1));
        Mockito.when(postCommentsRepository.findById(postComment.getId())).thenReturn(Optional.of(postComment));

        Assertions.assertInstanceOf(String.class, this.commentService.deleteCommentById(postComment.getId()));
    }


}
