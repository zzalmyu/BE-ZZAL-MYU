package com.prgrms.be.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), GUEST("ROLE_GUEST");

    private final String key;
}
