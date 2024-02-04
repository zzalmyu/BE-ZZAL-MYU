package com.prgrms.be.domain.user.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

//"/login" 요청 왔을 때 json 값 처리하는 필터
public class CustomJsonUsernamePasswordAuthenticationFilter extends
    AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERNAME_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
        new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL,
            HTTP_METHOD); //"/login" + POST로 온 요청에 매칭됨
    private final ObjectMapper objectMapper;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "/login" + POST로 온 요청 처리
        this.objectMapper = objectMapper;
    }

    // 1. request에서 username, password 추출하기
    // 2. usernamePassword 인증 토큰 만들기
    // 3. 2번에서 만든 토큰을 AuthenticationManager(ProviderManager)로 넘기기(AuthenticationManager는 인증 성공/실패 처리해줌)
    //    - ProviderManager의 구현체로 DaoAuthenticationProvider 사용
    // 4. LoginService에서 계속~~
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported");
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(),
            StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            email, password);

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
