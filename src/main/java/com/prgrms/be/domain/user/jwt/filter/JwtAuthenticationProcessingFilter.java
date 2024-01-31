package com.prgrms.be.domain.user.jwt.filter;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.infrastructure.UserJPARepository;
import com.prgrms.be.domain.user.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";

    private final JwtService jwtService;
    private final UserJPARepository userJPARepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if(refreshToken != null) {
            checkRefreshTokenAndReissueAccessToken(response, refreshToken);
            return;
        }

        if(refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    private void checkRefreshTokenAndReissueAccessToken(HttpServletResponse response, String refreshToken) {
        userJPARepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reissuedRefreshToken = reissueRefreshToken(user);
                    jwtService.sendAccessTokenAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()), reissuedRefreshToken);
                });

    }

    private String reissueRefreshToken(User user) {
        String reissuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reissuedRefreshToken);
        userJPARepository.saveAndFlush(user);
        return reissuedRefreshToken;
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userJPARepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAuthentication(User myUser) {
        String password = getPassword(myUser);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getPassword(User myUser) {
        String password = myUser.getPassword();
        if(password == null) {
            password = UUID.randomUUID().toString();
        }

        return password;
    }
}
