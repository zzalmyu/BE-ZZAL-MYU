package com.prgrms.zzalmyu.domain.user.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.oauth2.CustomOAuth2User;
import com.prgrms.zzalmyu.domain.user.oauth2.LoginSuccessResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

        //accessToken, refreshToken을 헤더에 넣고, 응답 dto 구성
        LoginSuccessResponse loginSuccessResponse = LoginSuccessResponse.of(email, oAuth2User.getNickname(),
            oAuth2User.getRole());
        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);

        // redis의 리프레시 토큰 새로 발급한 리프레시 토큰으로 갱신
        jwtService.updateRefreshToken(refreshToken, email);

        //응답 보내기
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(loginSuccessResponse));
        response.getWriter().flush();
    }
}
