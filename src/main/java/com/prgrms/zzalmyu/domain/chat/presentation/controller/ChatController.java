package com.prgrms.zzalmyu.domain.chat.presentation.controller;

import com.prgrms.zzalmyu.domain.chat.application.ChatService;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatOldMessageResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @ApiResponse(description = "채팅 내역 불러오기")
    @GetMapping
    public List<ChatOldMessageResponse> getOldChats(@PageableDefault(size = 10) Pageable pageable) {
        return chatService.getOldChats(pageable);
    }
}
