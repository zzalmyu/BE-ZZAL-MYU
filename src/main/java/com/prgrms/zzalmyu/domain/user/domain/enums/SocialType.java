package com.prgrms.zzalmyu.domain.user.domain.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SocialType {
    GOOGLE("google"), KAKAO("kakao"), NAVER("naver");

    private final String label;

    SocialType(String label) {
        this.label = label;
    }

    private static final Map<String, SocialType> BY_LABEL =
        Stream.of(values()).collect(Collectors.toMap(SocialType::label, socialType -> socialType));

    public static SocialType getSocialTypeByLabel(String registrationId) {
        return BY_LABEL.get(registrationId);
    }

    public String label() {
        return label;
    }
}
