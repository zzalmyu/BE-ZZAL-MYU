package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import com.prgrms.zzalmyu.domain.chat.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponse {

    private String email;

    private String nickname;

    private String message;

    private MessageType type;

    public static ChatResponse of(String email, String nickname, String message, MessageType type) {
        return new ChatResponse(email, nickname, message, type);
    }
}
