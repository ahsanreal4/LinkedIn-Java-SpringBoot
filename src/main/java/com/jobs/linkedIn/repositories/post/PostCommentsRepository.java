package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    Set<PostComment> findByPostIdAndParentId(long postId, Long parentId);
    int countByPostId(long postId);
    Set<PostComment> findByParentId(long id);
}
