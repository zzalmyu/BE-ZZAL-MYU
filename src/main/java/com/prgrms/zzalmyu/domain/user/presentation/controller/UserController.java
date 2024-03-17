package com.prgrms.zzalmyu.domain.user.presentation.controller;

import com.prgrms.zzalmyu.domain.user.application.UserService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.presentation.dto.res.UserInfoResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @ApiResponse(description = "토큰 재발급 요청")
    @PostMapping("/reissue")
    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueTokens(request, response);
    }
    @ApiResponse(description = "로그아웃")
    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(description = "유저 탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> withdraw(HttpServletRequest request, @AuthenticationPrincipal User user) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        userService.withdraw(user.getId(), accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(description = "유저 정보 반환")
    @GetMapping("/info")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal User user) {
        return UserInfoResponse.of(user);
    }
}
