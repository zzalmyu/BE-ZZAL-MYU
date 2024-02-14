package com.prgrms.zzalmyu.domain.user.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.prgrms.zzalmyu.common.redis.RedisService;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String createAccessToken(String email) {
        Date now = new Date();

        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken,
        String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, BEARER + accessToken);
        setRefreshTokenHeader(response, BEARER + refreshToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
            .filter(accessToken -> accessToken.startsWith(BEARER))
            .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken) //검증
                .getClaim(EMAIL_CLAIM) //추출
                .asString());
        } catch (JWTVerificationException e) {
            throw new UserException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
    }

    //RefreshToken redis 저장
    public void updateRefreshToken(String refreshToken, String email) {
        redisService.setValues(refreshToken, email,
            Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new UserException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new UserException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void logoutAccessToken(String accessToken) {
        redisService.setValues(accessToken, "logout",
            Duration.ofMillis(accessTokenExpirationPeriod));
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }
}