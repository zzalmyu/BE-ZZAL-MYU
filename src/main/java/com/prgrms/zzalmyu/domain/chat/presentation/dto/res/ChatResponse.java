package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponse {
    private String email;
    private String nickname;
    private String message;

    public static ChatResponse of(String email, String nickname, String message) {
        return new ChatResponse(email, nickname, message);
    }
}
