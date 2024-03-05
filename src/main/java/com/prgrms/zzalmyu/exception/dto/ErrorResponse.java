package com.prgrms.zzalmyu.exception.dto;

import com.nimbusds.oauth2.sdk.token.AccessTokenType;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String statusCodeName;
    private final String code;
    private final String message;
    private final String runtimeValue;
    private static final AccessTokenType BEARER = new AccessTokenType("Bearer ");

    public static ResponseEntity<ErrorResponse> toResponseEntity(
        ErrorCode errorCode, String runtimeValue
    ) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .statusCodeName(errorCode.getHttpStatus().name())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .runtimeValue(runtimeValue)
                .build()
            );
    }

    public static String extractAccessToken(String runtimeValue) {
        StringTokenizer st = new StringTokenizer(runtimeValue);

        return BEARER + st.nextToken();
    }

    public static String extractRefreshToken(String runtimeValue) {
        StringTokenizer st = new StringTokenizer(runtimeValue);
        st.nextToken();

        return BEARER + st.nextToken();
    }
}
