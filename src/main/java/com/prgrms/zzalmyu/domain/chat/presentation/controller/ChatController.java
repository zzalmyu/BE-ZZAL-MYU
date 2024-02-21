package com.prgrms.zzalmyu.domain.chat.presentation.controller;

import com.prgrms.zzalmyu.domain.chat.presentation.dto.req.ChatNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(ChatNameRequest request) {
        return HtmlUtils.htmlEscape(request.getName()) + "님이 입장하셨습니다.";
    }
}
