package com.prgrms.zzalmyu.domain.chat.infrastructure;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatOldMessageResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c")
    List<ChatMessage> findForOneDay(Pageable pageable);
}
