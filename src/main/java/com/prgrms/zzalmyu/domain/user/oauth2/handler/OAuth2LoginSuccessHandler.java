package com.prgrms.zzalmyu.domain.user.oauth2.handler;

import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// Authorization Code 인증 성공 시
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private static final String BEARER = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        //accessToken, refreshToken 생성
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        //accessToken, refreshToken 발급하고 /home으로 리다이렉트하도록 응답 보내기
        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        response.sendRedirect("/home");

        // redis의 리프레시 토큰 새로 발급한 리프레시 토큰으로 갱신
        jwtService.updateRefreshToken(email, refreshToken);
    }
}
