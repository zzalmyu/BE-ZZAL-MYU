package com.prgrms.zzalmyu.domain.user.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException) {

        setErrorResponse(response, ErrorCode.SECURITY_ACCESS_DENIED);
    }

    private void setErrorResponse(
        HttpServletResponse response,
        ErrorCode errorCode
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
            response.getWriter().write(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}