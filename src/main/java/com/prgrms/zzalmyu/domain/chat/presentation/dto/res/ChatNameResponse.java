package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatNameResponse {

    private String nickname;

    private String email;

    public static ChatNameResponse of(String email, String nickname) {
        return ChatNameResponse.builder()
            .email(email)
            .nickname(nickname)
            .build();
    }
}
