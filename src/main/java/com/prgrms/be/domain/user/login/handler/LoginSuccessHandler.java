package com.prgrms.be.domain.user.login.handler;

import com.prgrms.be.domain.user.infrastructure.UserJPARepository;
import com.prgrms.be.domain.user.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

// 1. 인증 객체에서 email 추출하고, AccessToken과 RefreshToken을 생성
// 2. 1번의 세 정보를 response에 넣어서 보내기
// 3. user의 RefreshToken 넣어주기
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserJPARepository userJPARepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String email = extractUsernameFromAuthentication(authentication);
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);

        userJPARepository.findByEmail(email)
            .ifPresent(user -> {
                user.updateRefreshToken(refreshToken);
                userJPARepository.saveAndFlush(user);
            });
    }

    private String extractUsernameFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
