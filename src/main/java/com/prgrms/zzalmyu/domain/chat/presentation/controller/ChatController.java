package com.prgrms.zzalmyu.domain.chat.presentation.controller;

import com.prgrms.zzalmyu.domain.chat.application.ChatService;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.req.ChatNameRequest;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatNameResponse;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatOldMessageResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @ApiResponse(description = "채팅 내역 불러오기")
    @GetMapping()
    public List<ChatOldMessageResponse> getOldChats(@PageableDefault(size = 10) Pageable pageable) {
        return chatService.getOldChats(pageable);
    }

    @ApiResponse(description = "채팅 이름 생성하기")
    @PostMapping("/nickname")
    public ResponseEntity<ChatNameResponse> getChatName(@RequestBody ChatNameRequest request) {
        ChatNameResponse response = chatService.generateNickname(request);
        return ResponseEntity.ok(response);
    }
}
