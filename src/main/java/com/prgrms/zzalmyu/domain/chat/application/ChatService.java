package com.prgrms.zzalmyu.domain.chat.application;

import com.prgrms.zzalmyu.common.redis.RedisService;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import com.prgrms.zzalmyu.domain.chat.domain.enums.MessageType;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ChatMessageRepository;
import com.prgrms.zzalmyu.domain.chat.presentation.dto.res.ChatOldMessageResponse;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final RedisService redisService;
    private final ChatMessageRepository chatMessageRepository;

    private static final String NOT_EXIST = "false";
    private static final String HELLO_MESSAGE_SUFFIX = "님이 입장하셨습니다.";

    @Value("${jwt.refresh.expiration}")
    private Long nicknameExpirationPeriod;

    String[] suffix = { "순", "식", "돌", "민", "숙", "둥",
                        "동", "석", "갑", "복", "진", "윤",
                        "준", "범", "섭", "숭", "익", "용" };

    public String generateNickname() {
        Random random = new Random();

        // 0부터 size까지의 랜덤 수 생성
        int randomNumber = random.nextInt(suffix.length);
        return "짤" + suffix[randomNumber] + "이";
    }

    public void saveNickname(String email, String nickname) {
        redisService.setValues(email, nickname, Duration.ofMillis(nicknameExpirationPeriod));
    }

    @Transactional(readOnly = true)
    public String getNickname(String email) {
        String nickname = redisService.getValues(email);
        if(nickname.equals(NOT_EXIST)) {
            throw new UserException(ErrorCode.CHAT_NICKNAME_NOT_FOUND);
        };
        return nickname;
    }

    public void deleteChatNickname(String email) {
        redisService.delete(email);
    }

    public String saveHelloMessage(String email, String nickname) {
        String message = nickname + HELLO_MESSAGE_SUFFIX;
        ChatMessage chatMessage = ChatMessage.builder()
            .email(email)
            .nickname(nickname)
            .message(message)
            .type(MessageType.HELLO)
            .build();
        chatMessageRepository.save(chatMessage);

        return message;
    }

    public void saveImageMessage(String email, String nickname, String image) {
        ChatMessage chatMessage = ChatMessage.builder()
            .email(email)
            .nickname(nickname)
            .message(image)
            .type(MessageType.IMAGE)
            .build();
        chatMessageRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatOldMessageResponse> getOldChats(Pageable pageable) {
        return chatMessageRepository.findAllByLatest(pageable)
            .stream()
            .map(message -> ChatOldMessageResponse.of(
                message.getNickname(),
                message.getMessage(),
                message.getCreatedAt(),
                message.getType(),
                    message.getEmail()
            ))
            .collect(Collectors.toList());
    }
}
