package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
