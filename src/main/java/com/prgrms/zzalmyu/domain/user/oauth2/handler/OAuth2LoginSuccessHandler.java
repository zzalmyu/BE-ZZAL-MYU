package com.prgrms.zzalmyu.domain.user.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.oauth2.CustomOAuth2User;
import com.prgrms.zzalmyu.domain.user.oauth2.LoginSuccessResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

// Authorization Code 인증 성공 시
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        //accessToken, refreshToken 생성
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        // redis의 리프레시 토큰 새로 발급한 리프레시 토큰으로 갱신
        jwtService.updateRefreshToken(refreshToken, email);

        //응답 보내기
        response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:5173")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString());
    }
}
