package com.jobs.linkedIn.repositories.user;

import com.jobs.linkedIn.entities.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
