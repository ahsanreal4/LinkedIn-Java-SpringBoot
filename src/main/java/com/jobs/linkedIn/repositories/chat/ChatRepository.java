package com.jobs.linkedIn.repositories.chat;

import com.jobs.linkedIn.entities.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
