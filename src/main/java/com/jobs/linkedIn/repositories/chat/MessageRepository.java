package com.jobs.linkedIn.repositories.chat;

import com.jobs.linkedIn.entities.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
