package com.jobs.linkedIn.unit.services.post;

import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.impl.post.PostServiceImpl;
import com.jobs.linkedIn.services.interfaces.post.CommentService;
import com.jobs.linkedIn.services.interfaces.post.PostLikeService;
import com.jobs.linkedIn.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private PostLikeService postLikeService;

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

    Post getNewPost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Some post");
        post.setDescription("Some descirption");

        return post;
    }

    CreatePostDto getNewCreatePostDto(Post post) {
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTitle(post.getTitle());
        createPostDto.setDescription(post.getDescription());

        return createPostDto;
    }

    PostDto getNewPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostedAt(post.getPostedAt());
        postDto.setLiked(false);
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());

        return postDto;
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void SHOULD_SAVE() {
        User user = getNewUser();
        Post post = getNewPost();
        post.setPostedBy(user);
        CreatePostDto createPostDto = getNewCreatePostDto(post);
        PostDto postDto = getNewPostDto(post);
        postDto.setPostedBy(user.getId());

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

        Assertions.assertNotSame(null, this.postService.createPost(createPostDto));
    }

    @Test
    void SHOULD_SAVE_MULTIPLE_POSTS() {
        User user = getNewUser();
        Post post = getNewPost();
        post.setPostedBy(user);
        CreatePostDto createPostDto = getNewCreatePostDto(post);
        CreatePostDto createPostDto1 = getNewCreatePostDto(post);
        createPostDto1.setTitle("changed");
        PostDto postDto = getNewPostDto(post);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

        Assertions.assertNotSame(null, this.postService.createPost(createPostDto));
        Assertions.assertNotSame(null, this.postService.createPost(createPostDto1));
    }

    @Test
    void SHOULD_RETURN_POST_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost();
        post.setPostedBy(user);
        List<PostCommentDto> commentDtos = new ArrayList<>();

        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentService.getPostComments(post.getId())).thenReturn(commentDtos);
        Mockito.when(postLikeService.getNumberOfPostLikes(post.getId())).thenReturn(0);

        Assertions.assertInstanceOf(PostDto.class, this.postService.getPostById(post.getId()));
    }

    @Test
    void SHOULD_NOT_RETURN_POST_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost();
        post.setPostedBy(user);
        List<PostCommentDto> commentDtos = new ArrayList<>();

        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.empty());
        Mockito.when(commentService.getPostComments(post.getId())).thenReturn(commentDtos);
        Mockito.when(postLikeService.getNumberOfPostLikes(post.getId())).thenReturn(0);

        Assertions.assertThrows(ApiException.class, () -> {
            this.postService.getPostById(post.getId());
        });
    }

    @Test
    void SHOULD_DELETE_POST_BY_ID() {
        User user = getNewUser();
        Post post = getNewPost();
        post.setPostedBy(user);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Assertions.assertInstanceOf(String.class, this.postService.deletePostById(post.getId()));
    }

    @Test
    void SHOULD_NOT_DELETE_SOMEONE_ELSE_POST() {
        User user = getNewUser();
        User user1 = getNewUser();
        user1.setId(2L);
        Post post = getNewPost();
        post.setPostedBy(user1);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(false);

        Assertions.assertThrows(ApiException.class, () -> {
            postService.deletePostById(post.getId());
        });
    }

    @Test
    void SHOULD_DELETE_SOMEONE_ELSE_POST_WHEN_ADMIN() {
        User user = getNewUser();
        User user1 = getNewUser();
        user1.setId(2L);
        Post post = getNewPost();
        post.setPostedBy(user1);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userUtils.isAdmin(user.getRoles())).thenReturn(true);

        Assertions.assertInstanceOf(String.class, this.postService.deletePostById(post.getId()));
    }

}
