package com.prgrms.zzalmyu.domain.user.presentation.controller;

import com.prgrms.zzalmyu.domain.user.application.UserService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String accessToken = jwtService.extractAccessToken(request).get();
        String refreshToken = jwtService.extractRefreshToken(request).get();
        userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(description = "유저 탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal User user) {
        userService.withdraw(user.getId());
        return ResponseEntity.noContent().build();
    }

    //    테스트용 컨트롤러
    @ApiResponse(description = "테스트용 (무시해주세요)")
    @GetMapping("/jwt-test")
    public String jwtTest(@AuthenticationPrincipal User user) {
        return "jwtTest 요청 성공";
    }
}
