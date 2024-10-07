package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
}
