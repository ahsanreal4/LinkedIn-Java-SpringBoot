package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdAndParentId(long postId, Long parentId);
    int countByPostId(long postId);
    List<PostComment> findByParentId(long id);
}
