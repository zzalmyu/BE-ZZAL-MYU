package com.prgrms.zzalmyu.domain.chat.presentation.dto.req;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatNameRequest {

    private String name;
}
