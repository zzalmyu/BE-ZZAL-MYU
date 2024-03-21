package com.prgrms.zzalmyu.domain.user.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.chat.application.ChatService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ChatService chatService;

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
            .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);

        jwtService.reissueAndSendTokens(response, refreshToken);
    }

    public void logout(Optional<String> accessToken, Optional<String> refreshToken) {
        String access = accessToken
            .orElseThrow(() -> new UserException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
            .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        String email = jwtService.extractEmail(access)
            .orElseThrow(() -> new UserException(ErrorCode.EMAIL_NOT_EXTRACTED));
        chatService.deleteChatNickname(email);

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        //refresh token 삭제
        jwtService.deleteRefreshToken(refresh);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);
    }

    public void withdraw(Long id, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = findUserById(id);
        user.delete();

        String access = accessToken
            .orElseThrow(() -> new UserException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
            .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        jwtService.deleteRefreshToken(refresh);
        jwtService.invalidAccessToken(access);
    }

    public User findUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
