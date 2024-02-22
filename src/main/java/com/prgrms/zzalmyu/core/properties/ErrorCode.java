package com.prgrms.zzalmyu.core.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    IMAGE_NOT_FOUND_ERROR(BAD_REQUEST, "이미지가 존재하지 않습니다."),
    IMAGE_ONLY_UPLOAD_USER_DELETE(BAD_REQUEST,"업로드한 사용자만 삭제 가능할 수 있습니다."),
    IMAGE_ALREADY_LIKE(BAD_REQUEST,"이미 좋아요를 누른 이미지 입니다."),
    IMAGE_ALREADY_LIKE_CANCLE(BAD_REQUEST,"이미 좋아요 취소를 누른 이미지 입니다."),
    ADMIN_ONLY_REPORT_DELETE(BAD_REQUEST,"관리자만이 신고 이미지를 처리할 수 있습니다."),
    REPORT_NOT_FOUND(BAD_REQUEST, "이미지에 대한 신고 내역이 존재하지 않습니다."),
    REPORT_ALREADY_EXIST_ERROR(BAD_REQUEST, "이미 해당 이미지를 신고한 사용자입니다."),
    USER_NOT_FOUND(BAD_REQUEST, "user을 찾을 수 없습니다."),
    IMAGE_DELETION_NOT_ALLOWED(BAD_REQUEST, "신고가 3번 이상 누적되어야 이미지를 삭제할 수 있습니다."),
    TAG_NOT_FOUND_ERROR(BAD_REQUEST, "해당 태그가 존재하지 않습니다,"),
    TAG_ALREADY_EXIST_ERROR(BAD_REQUEST, "해당하는 태그가 이미 존재합니다."),


    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SOCIAL_LOGIN_FAIL(UNAUTHORIZED, "소셜 로그인에 실패했습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
