package com.jobs.linkedIn.repository.post;

import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.user.User;
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
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

     User user;

    Post getNewPost() {
        Post post = new Post();

        post.setDescription("some description");
        post.setPostedAt(new Date());
        post.setTitle("Some title");
        post.setPostedBy(user);

        return post;
    }

    @BeforeEach()
    void setUp() {
        user = new User();
        user.setUsername("ahsanreal5315314");
        user.setEmail("ahsan.btph153153123@gmail.com");
        user.setPassword("axeem0099");
        user.setFirstName("Ahsan");
        user.setLastName("Azeem");

        userRepository.save(user);
    }

    @Test
    void SHOULD_SAVE() {
        Post post = getNewPost();

        Post savedPost = postRepository.save(post);

        Assertions.assertNotSame(savedPost, null);
    }

    @Test
    void SHOULD_SAVE_MULTIPLE_POSTS() {
        Post post = getNewPost();
        Post post1 = getNewPost();
        post1.setTitle("Some other title");

        postRepository.save(post);
        postRepository.save(post1);
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_TITLES() {
        Post post = getNewPost();

        Post post2 = getNewPost();

        postRepository.save(post);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            postRepository.save(post2);
        });
    }
}
