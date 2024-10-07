package com.jobs.linkedIn.repositories.user;

import com.jobs.linkedIn.entities.user.UserFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFiles, Long> {
}
