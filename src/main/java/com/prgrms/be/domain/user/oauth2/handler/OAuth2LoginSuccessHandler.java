package com.prgrms.be.domain.user.oauth2.handler;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.domain.enums.Role;
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

        if (oAuth2User.getRole() == Role.GUEST) { // 첫 로그인 시 회원가입 페이지로 리다이렉트
            firstLogin(response, oAuth2User);
        } else {
            notFirstLogin(response, oAuth2User);
        }
    }

    private void firstLogin(HttpServletResponse response, CustomOAuth2User oAuth2User)
        throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        response.addHeader(jwtService.getAccessHeader(), BEARER + accessToken);
        response.sendRedirect("oauth2/sign-up"); //프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
        // 리다이렉트될 때는 헤더에 정보 담지 못함 -> URL에 쿼리 스트링으로 담거나 다른 방법 사용

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, null);
        // refresh token 발급 안한 이유: 빠른 사용자 경험 -> 일시적 세션으로 간주

        //TODO: 회원가입 추가 폼 입력 시 업데이트 하는 컨트롤러, 서비스 만들면 주석 해제
//        updateAuthorizationToUser(oAuth2User);
    }

    private void updateAuthorizationToUser(CustomOAuth2User oAuth2User) {
        User findUser = userJPARepository.findByEmail(oAuth2User.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
        findUser.authorizeUser();
    }

    // 첫 로그인이 아닐 때 access, refresh 토큰 생성
    // TODO: 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유무에 따라 처리하기
    private void notFirstLogin(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), BEARER + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), BEARER + refreshToken);

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
