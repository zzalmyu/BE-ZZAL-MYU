package com.prgrms.zzalmyu.domain.user.oauth2;

import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSuccessResponse {

    private String email;

    private Role role;

    public static LoginSuccessResponse of(String email, Role role) {
        return LoginSuccessResponse.builder()
            .email(email)
            .role(role)
            .build();
    }
}
