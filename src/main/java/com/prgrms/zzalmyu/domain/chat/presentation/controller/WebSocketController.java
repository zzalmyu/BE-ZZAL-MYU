package com.prgrms.zzalmyu.domain.chat.presentation.controller;

import com.prgrms.zzalmyu.domain.chat.application.ChatService;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.req.ChatPhotoRequest;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;

    @MessageMapping("/image")
    public void sendPhoto(ChatPhotoRequest request) {
        String nickname = chatService.getNickname(request.getEmail());
        chatService.saveImageMessage(request.getEmail(), nickname, request.getImage());
        simpMessageSendingOperations.convertAndSend("/sub/" + request.getChannelId(), ChatResponse.of(request.getEmail(), nickname, request.getImage()));
    }
}
