package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import com.prgrms.zzalmyu.domain.chat.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatNameResponse {

    private String nickname;

    private String email;

    public static ChatNameResponse of(String email, String nickname) {
        return new ChatNameResponse(email, nickname);
    }
}
