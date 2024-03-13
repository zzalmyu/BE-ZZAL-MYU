package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatHelloResponse {
    private String email;
    private String nickname;
    private String message;

    public static ChatHelloResponse of(String email, String nickname, String message) {
        return new ChatHelloResponse(email, nickname, message);
    }
}
