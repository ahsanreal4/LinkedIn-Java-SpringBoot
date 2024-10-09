package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    PostLikes findByUserIdAndPostId(long userId, long postId);
    int countByPostId(long postId);
    boolean existsByUserIdAndPostId(long userId, long postId);
    List<PostLikes> findByPostId(long postId);
}
