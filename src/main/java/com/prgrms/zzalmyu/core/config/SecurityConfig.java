package com.prgrms.zzalmyu.core.config;

import com.prgrms.zzalmyu.common.redis.RedisService;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import com.prgrms.zzalmyu.domain.user.jwt.entrypoint.CustomAuthenticationEntryPoint;
import com.prgrms.zzalmyu.domain.user.jwt.filter.ExceptionHandlerFilter;
import com.prgrms.zzalmyu.domain.user.jwt.filter.JwtAuthenticationProcessingFilter;
import com.prgrms.zzalmyu.domain.user.jwt.handler.CustomAccessDeniedHandler;
import com.prgrms.zzalmyu.domain.user.jwt.service.JwtService;
import com.prgrms.zzalmyu.domain.user.oauth2.handler.OAuth2LoginFailureHandler;
import com.prgrms.zzalmyu.domain.user.oauth2.handler.OAuth2LoginSuccessHandler;
import com.prgrms.zzalmyu.domain.user.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
                .requestMatchers("/api/v1/user/reissue").permitAll()
                .requestMatchers("/api/v1/user/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/tag", "/api/v1/tag/search", "/api/v1/tag/popular").permitAll()
                .requestMatchers("/api/v1/tag/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/report/**").authenticated()
                .requestMatchers("/api/v1/report/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/image", "/api/v1/image/{imageId}").permitAll()
                .requestMatchers("/api/v1/image/**").authenticated()
                .requestMatchers("/api/v1/chat").permitAll()
                .requestMatchers("/chat").permitAll())
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler()))
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndPoint -> userInfoEndPoint
                    .userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(
                    oAuth2LoginFailureHandler)) // defaultSuccessUrl 설정 안해주면 사용자가 처음 접근했던 페이지로 리다이렉트됨
            .addFilterAfter(jwtAuthenticationProcessFilter(), LogoutFilter.class)
            .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationProcessingFilter.class);
        // 필터 순서: Logout filter -> jwtAuthenticationProcessFilter
        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, redisService, userRepository);
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
