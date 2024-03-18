package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import com.prgrms.zzalmyu.domain.chat.domain.enums.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatOldMessageResponse {

    private String nickname;

    private String message;

    private LocalDateTime createdAt;

    private MessageType type;

    private String email;

    public static ChatOldMessageResponse of(String nickname, String message, LocalDateTime createdAt, MessageType type, String email) {
        return ChatOldMessageResponse.builder()
            .nickname(nickname)
            .message(message)
            .createdAt(createdAt)
            .type(type)
                .email(email)
            .build();
    }
}
