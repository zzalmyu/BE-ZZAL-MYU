package com.prgrms.zzalmyu.domain.user.jwt.filter;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.user.application.RedisService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserJPARepository;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserJPARepository userJPARepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String refreshToken = jwtService.extractRefreshToken(request)
            .filter(jwtService::isTokenValid)
            .orElse(null); // 리프레시 토큰 만료 기간이 끝나 유효성 검사에서 걸려서 null이 되었을 경우

        if (refreshToken != null) {
            checkRefreshTokenAndReissueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
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

        if (email.equals("false")) {
            throw new UserException(ErrorCode.SECURITY_INVALID_TOKEN);
        }
        return email;
    }

    private String reissueRefreshToken(String email) {
        String reissuedRefreshToken = jwtService.createRefreshToken();
        redisService.setValues(reissuedRefreshToken, email,
            Duration.ofMillis(jwtService.getRefreshTokenExpirationPeriod()));
        return reissuedRefreshToken;
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
            .filter(jwtService::isTokenValid).flatMap(jwtService::extractEmail)
            .flatMap(userJPARepository::findByEmail).ifPresent(this::saveAuthentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new UserException(ErrorCode.SERVER_ERROR);
        }
    }

    private void saveAuthentication(User myUser) {
        String password = getTemporaryPassword();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(myUser.getEmail())
            .password(password)
            .roles(myUser.getRole().name())
            .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTemporaryPassword() {
        return UUID.randomUUID().toString();
    }
}
