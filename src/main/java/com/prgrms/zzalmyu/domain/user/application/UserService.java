package com.prgrms.zzalmyu.domain.user.application;

import com.prgrms.zzalmyu.domain.user.infrastructure.UserJPARepository;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJPARepository userJPARepository;
    private final JwtService jwtService;

    public void logout(String accessToken, String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);

        //refresh token 삭제
        jwtService.deleteRefreshToken(refreshToken);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.logoutAccessToken(accessToken);
    }
}
