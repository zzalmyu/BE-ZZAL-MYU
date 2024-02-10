package com.prgrms.zzalmyu.domain.user.presentation.controller;

import com.prgrms.zzalmyu.domain.user.application.UserService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request).get();
        String refreshToken = jwtService.extractRefreshToken(request).get();
        userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    //    테스트용 컨트롤러
    @GetMapping("/jwt-test")
    public String jwtTest(@AuthenticationPrincipal User user) {
        return "jwtTest 요청 성공";
    }
}
