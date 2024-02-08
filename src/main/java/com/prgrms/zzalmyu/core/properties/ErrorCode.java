package com.prgrms.zzalmyu.core.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    IMAGE_NOT_FOUND_ERROR(BAD_REQUEST, "이미지가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
