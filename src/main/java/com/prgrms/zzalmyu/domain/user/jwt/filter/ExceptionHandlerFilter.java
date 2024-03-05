package com.prgrms.zzalmyu.domain.user.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.user.exception.UserException;
import com.prgrms.zzalmyu.exception.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.prgrms.zzalmyu.exception.dto.ErrorResponse.extractAccessToken;
import static com.prgrms.zzalmyu.exception.dto.ErrorResponse.extractRefreshToken;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (UserException e) {
            setErrorResponse(response, e.getErrorCode(), e.getRuntimeValue());
        } catch (TokenExpiredException e) {
            setErrorResponse(response, ErrorCode.SECURITY_INVALID_TOKEN, null);
        } catch (AuthenticationException | JWTVerificationException e) {
            setErrorResponse(response, ErrorCode.SECURITY_UNAUTHORIZED, null);
        } catch (AccessDeniedException e) {
            setErrorResponse(response, ErrorCode.SECURITY_ACCESS_DENIED, null);
        }
    }

    private void setErrorResponse(
        HttpServletResponse response,
        ErrorCode errorCode,
        String runtimeValue
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .statusCode(errorCode.getHttpStatus().value())
            .statusCodeName(errorCode.getHttpStatus().name())
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();
        try {
            if(errorCode == ErrorCode.SECURITY_INVALID_ACCESS_TOKEN) {
                String accessToken = extractAccessToken(runtimeValue);
                String refreshToken = extractRefreshToken(runtimeValue);
                response.setHeader("Authorization", accessToken);
                response.setHeader("Authorization-refresh", refreshToken);
            }
            response.getWriter().write(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
