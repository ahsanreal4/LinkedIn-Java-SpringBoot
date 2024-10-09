package com.jobs.linkedIn.repository.post;

import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostComment;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.repositories.post.PostCommentsRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostCommentRepositoryTests {

    @Autowired
    PostCommentsRepository postCommentsRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Post post;

    User getNewUser() {
        User currUser = new User();
        currUser.setUsername("ahsanreal453513");
        currUser.setEmail("ahsan.btph123531531@gmail.com");
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

    PostComment getNewPostComment() {
        PostComment postComment = new PostComment();
        postComment.setUser(user);
        postComment.setPost(post);
        postComment.setText("Some random text");
        postComment.setPostedAt(new Date());

        return postComment;
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
        PostComment postComment = getNewPostComment();

        PostComment savedComment = postCommentsRepository.save(postComment);

        Assertions.assertNotSame(savedComment, null);
    }


    @Test
    void SHOULD_SAVE_LONG_TEXT() {
        PostComment postComment = getNewPostComment();
        postComment.setText("gfgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");

        PostComment savedComment = postCommentsRepository.save(postComment);

        Assertions.assertNotSame(savedComment, null);
    }

    @Test
    void SHOULD_SAVE_MULTIPLE_COMMENTS_FROM_USER() {
        PostComment postComment = getNewPostComment();
        PostComment postComment1 = getNewPostComment();

        PostComment savedComment = postCommentsRepository.save(postComment);
        PostComment savedComment2 = postCommentsRepository.save(postComment1);

        Assertions.assertNotSame(savedComment, null);
        Assertions.assertNotSame(savedComment2, null);
    }

    @Test
    void SHOULD_SAVE_FROM_DIFFERENT_USER() {
        PostComment comment = getNewPostComment();

        User user1 = getNewUser();
        user1.setUsername("ahsan");
        user1.setEmail("ahsan.btph2222@gmail.com");

        userRepository.save(user1);

        PostComment comment1 = getNewPostComment();
        comment1.setUser(user1);

        PostComment savedComment = postCommentsRepository.save(comment);
        PostComment savedComment2 = postCommentsRepository.save(comment1);

        Assertions.assertNotSame(savedComment, null);
        Assertions.assertNotSame(savedComment2, null);
    }

}
