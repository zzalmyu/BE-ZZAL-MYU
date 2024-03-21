package com.prgrms.zzalmyu.domain.chat.infrastructure;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c order by c.createdAt desc")
    List<ChatMessage> findAllByLatest(Pageable pageable);

    @Query(value = "select c from ChatMessage c where c.createdAt < :twoDaysAgo")
    Slice<ChatMessage> findBeforeTwoDays(LocalDateTime twoDaysAgo, Pageable pageable);
}
