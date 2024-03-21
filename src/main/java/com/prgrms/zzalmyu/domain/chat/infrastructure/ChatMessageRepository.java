package com.prgrms.zzalmyu.domain.chat.infrastructure;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c order by c.createdAt desc")
    List<ChatMessage> findAllByLatest(Pageable pageable);
}
