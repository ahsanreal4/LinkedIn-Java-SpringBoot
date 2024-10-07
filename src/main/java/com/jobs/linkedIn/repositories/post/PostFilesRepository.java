package com.jobs.linkedIn.repositories.post;

import com.jobs.linkedIn.entities.post.PostFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFilesRepository extends JpaRepository<PostFiles, Long> {
}
