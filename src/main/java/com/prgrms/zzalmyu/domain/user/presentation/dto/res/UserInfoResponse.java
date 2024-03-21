package com.prgrms.zzalmyu.domain.user.presentation.dto.res;

import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserInfoResponse {

    private Long userId;
    private String email;
    private Role role;

    private UserInfoResponse(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
