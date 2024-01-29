package com.prgrms.be.domain.user.presentation.dto.res;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserSignUpResponse {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String image;

    private Role role;

    private LocalDateTime createdAt;

    public static UserSignUpResponse from(User user) {
        return UserSignUpResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .image(user.getImage())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
