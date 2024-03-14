package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatOldMessageResponse {

    private String nickname;

    private String message;

    private LocalDateTime createdAt;

    public static ChatOldMessageResponse of(String nickname, String message, LocalDateTime createdAt) {
        return ChatOldMessageResponse.builder()
            .nickname(nickname)
            .message(message)
            .createdAt(createdAt)
            .build();
    }
}
