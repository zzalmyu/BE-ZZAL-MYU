package com.prgrms.zzalmyu.core.config;

import com.prgrms.zzalmyu.domain.user.application.RedisService;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import com.prgrms.zzalmyu.domain.user.jwt.filter.JwtAuthenticationProcessingFilter;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.oauth2.handler.OAuth2LoginFailureHandler;
import com.prgrms.zzalmyu.domain.user.oauth2.handler.OAuth2LoginSuccessHandler;
import com.prgrms.zzalmyu.domain.user.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .headers(headersConfigure -> headersConfigure
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/*").permitAll()
                .requestMatchers("/api/v1/user/jwt-test").authenticated()
                .requestMatchers("/**").permitAll())
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndPoint -> userInfoEndPoint
                    .userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(
                    oAuth2LoginFailureHandler)) // defaultSuccessUrl 설정 안해주면 사용자가 처음 접근했던 페이지로 리다이렉트됨
            .addFilterAfter(jwtAuthenticationProcessFilter(), LogoutFilter.class);
        // 필터 순서: Logout filter -> jwtAuthenticationProcessFilter
        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, redisService, userRepository);
    }
}
