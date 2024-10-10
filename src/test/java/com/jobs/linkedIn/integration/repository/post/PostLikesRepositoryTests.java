package com.jobs.linkedIn.integration.repository.post;

import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostLikes;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.repositories.post.PostLikesRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostLikesRepositoryTests {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostLikesRepository postLikesRepository;


    User user;
    Post post;

    User getNewUser() {
        User currUser = new User();
        currUser.setUsername("ahsanreal4531531");
        currUser.setEmail("ahsan.btph1253153@gmail.com");
        currUser.setPassword("axeem0099");
        currUser.setFirstName("Ahsan");
        currUser.setLastName("Azeem");

        return currUser;
    }

    Post getNewPost() {
        Post currPost = new Post();

        currPost.setDescription("some description");
        currPost.setPostedAt(new Date());
        currPost.setTitle("Some title");
        currPost.setPostedBy(user);

        return currPost;
    }

    PostLikes getNewPostLikes() {
        PostLikes postLikes = new PostLikes();

        postLikes.setUser(user);
        postLikes.setPost(post);

        return postLikes;
    }

    @BeforeEach()
    void setUp() {
        user = getNewUser();
        userRepository.save(user);

        post = getNewPost();
        postRepository.save(post);
    }


    @Test
    void SHOULD_SAVE() {
        PostLikes postLikes = getNewPostLikes();

        postLikesRepository.save(postLikes);
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_USER() {
        PostLikes postLikes = getNewPostLikes();
        PostLikes postLikes1 = getNewPostLikes();

        postLikesRepository.save(postLikes);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            postLikesRepository.save(postLikes1);
        });
    }
}
