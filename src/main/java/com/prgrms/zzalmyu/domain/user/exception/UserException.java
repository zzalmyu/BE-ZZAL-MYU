package com.prgrms.zzalmyu.domain.user.exception;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.exceptionClass.CustomException;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
