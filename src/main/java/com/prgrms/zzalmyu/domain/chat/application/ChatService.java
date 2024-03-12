package com.prgrms.zzalmyu.domain.chat.application;

import com.prgrms.zzalmyu.common.redis.RedisService;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String NOT_EXIST = "false";
    private final RedisService redisService;

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
}
