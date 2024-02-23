package com.prgrms.zzalmyu.domain.chat.presentation.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatHelloRequest {

    private String channelId;
    private String email;
}
