package com.prgrms.zzalmyu.exception.dto;

import java.time.LocalDateTime;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
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
}
