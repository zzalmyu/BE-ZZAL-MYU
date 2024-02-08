package com.prgrms.be.domain.user.oauth2.handler;

import com.prgrms.be.domain.user.infrastructure.UserJPARepository;
import com.prgrms.be.domain.user.jwt.service.JwtService;
import com.prgrms.be.domain.user.oauth2.CustomOAuth2User;
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
    private final UserJPARepository userJPARepository;

    private static final String BEARER = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        notFirstLogin(response, oAuth2User);
    }

    // 첫 로그인이 아닐 때 access, refresh 토큰 생성
    // TODO: 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유무에 따라 처리하기
    private void notFirstLogin(HttpServletResponse response, CustomOAuth2User oAuth2User)
        throws IOException {
        String email = oAuth2User.getEmail();
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        response.addHeader(jwtService.getAccessHeader(), BEARER + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), BEARER + refreshToken);
        response.sendRedirect("/home");

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(email, refreshToken);
    }
}
