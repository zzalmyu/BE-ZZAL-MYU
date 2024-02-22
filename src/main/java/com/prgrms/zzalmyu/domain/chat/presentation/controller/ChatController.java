package com.prgrms.zzalmyu.domain.chat.presentation.controller;

import com.prgrms.zzalmyu.domain.chat.presentation.dto.req.ChatNameRequest;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.req.ChatPhotoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/hello")
    public void greeting(ChatNameRequest request) {
        log.info("해윙 " + request.getName());
        simpMessageSendingOperations.convertAndSend("/sub/" + request.getChannelId(), request.getName() + " 님이 입장하셨습니다.");
    }

    @MessageMapping("/image")
    public void sendPhoto(ChatPhotoRequest request) {
        log.info("사진 보낸당");
        simpMessageSendingOperations.convertAndSend("/sub/" + request.getChannelId(), request.getImage());
    }
}
