package com.prgrms.zzalmyu.domain.chat.application;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ChatService {

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
    }
}
