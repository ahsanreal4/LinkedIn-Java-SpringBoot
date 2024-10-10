package com.jobs.linkedIn.unit.services.post;

import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostLikes;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostLikesRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.impl.post.PostLikeServiceImpl;
import com.jobs.linkedIn.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PostLikeServiceTest {
    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikesRepository postLikesRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

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


    @Test
    void SHOULD_LIKE_POST() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostLikes postLikes = new PostLikes();

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(postLikesRepository.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(null);
        Mockito.when(postLikesRepository.save(Mockito.any(PostLikes.class))).thenReturn(postLikes);

        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
    }

    @Test
    void SHOULD_UNLIKE_POST() {
        User user = getNewUser();
        Post post = getNewPost(user);
        PostLikes postLikes = new PostLikes();

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(postLikesRepository.findByUserIdAndPostId(user.getId(), post.getId())).thenReturn(postLikes);
        Mockito.when(postLikesRepository.save(Mockito.any(PostLikes.class))).thenReturn(postLikes);

        Assertions.assertInstanceOf(String.class, this.postLikeService.likePost(post.getId()));
    }

    @Test
    void SHOULD_RETURN_IS_LIKED_BY_USER() {
        User user = getNewUser();
        Post post = getNewPost(user);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(postLikesRepository.existsByUserIdAndPostId(user.getId(),post.getId())).thenReturn(true);

        Assertions.assertInstanceOf(Boolean.class, this.postLikeService.isLikedByUser(post.getId()));
    }

    @Test
    void SHOULD_NOT_RETURN_IS_LIKED_BY_USER() {
        User user = getNewUser();
        Post post = getNewPost(user);

        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(postLikesRepository.existsByUserIdAndPostId(user.getId(),post.getId())).thenReturn(true);

        Assertions.assertThrows(ApiException.class, () -> {
            this.postLikeService.isLikedByUser(post.getId());
        });
    }
}
