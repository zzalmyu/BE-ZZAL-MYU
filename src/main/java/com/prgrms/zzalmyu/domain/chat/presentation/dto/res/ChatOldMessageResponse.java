package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatOldMessageResponse {

    private String nickname;

    private String message;

    private LocalDateTime createdAt;

    private String email;

    public static ChatOldMessageResponse of(String nickname, String message, LocalDateTime createdAt, String email) {
        return ChatOldMessageResponse.builder()
                .nickname(nickname)
                .message(message)
                .createdAt(createdAt)
                .email(email)
                .build();
    }
}
