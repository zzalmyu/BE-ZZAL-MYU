package com.prgrms.zzalmyu.domain.tag.exception;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.exceptionClass.CustomException;

public class TagException extends CustomException {

    public TagException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TagException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
