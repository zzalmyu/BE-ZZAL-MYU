package com.prgrms.zzalmyu.domain.image.exception;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.exceptionClass.CustomException;

public class ImageException extends CustomException {
    public ImageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ImageException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
