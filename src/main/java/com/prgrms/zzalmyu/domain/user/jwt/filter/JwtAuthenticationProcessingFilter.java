package com.prgrms.zzalmyu.domain.user.jwt.filter;

import com.prgrms.zzalmyu.common.redis.RedisService;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private static String NOT_EXIST = "false";

    private static String NO_CHECK_URL = "/api/v1/user/logout";

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        checkLogout(request); //로그아웃한 사용자면 인증 처리 안함

        jwtService.extractRefreshToken(request)
            .ifPresentOrElse(
                refreshToken -> {
                    if (jwtService.isTokenValid(refreshToken)) {
                        checkRefreshTokenAndReissueAccessToken(response, refreshToken);
                    } else {
                        throw new UserException(ErrorCode.SECURITY_INVALID_TOKEN);
                    }
                },
                () -> checkAccessTokenAndAuthentication(request, response, filterChain)
            );
    }

    private void checkLogout(HttpServletRequest request) {
        jwtService.extractAccessToken(request).ifPresent(accessToken -> {
            String value = redisService.getValues(accessToken);

            if (value.equals("logout")) {
                throw new UserException(ErrorCode.SECURITY_UNAUTHORIZED);
            }
        });
    }

    private void checkRefreshTokenAndReissueAccessToken(HttpServletResponse response,
        String refreshToken) {
        String email = findRefreshTokenAndExtractEmail(refreshToken);
        String reissuedRefreshToken = reissueRefreshToken(email);
        jwtService.sendAccessTokenAndRefreshToken(response, jwtService.createAccessToken(email),
            reissuedRefreshToken);
    }

    private String findRefreshTokenAndExtractEmail(String refreshToken) {
        String email = redisService.getValues(refreshToken);

        if (email.equals(NOT_EXIST)) {
            throw new UserException(ErrorCode.SECURITY_INVALID_TOKEN);
        }
        return email;
    }

    private String reissueRefreshToken(String email) {
        String reissuedRefreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(reissuedRefreshToken, email);
        return reissuedRefreshToken;
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
            .filter(jwtService::isTokenValid).flatMap(jwtService::extractEmail)
            .flatMap(userRepository::findByEmail).ifPresent(this::saveAuthentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new UserException(ErrorCode.SERVER_ERROR);
        }
    }

    private void saveAuthentication(User myUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(myUser, null,
            authoritiesMapper.mapAuthorities(myUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
