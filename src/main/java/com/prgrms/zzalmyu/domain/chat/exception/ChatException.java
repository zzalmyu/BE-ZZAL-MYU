package com.prgrms.zzalmyu.domain.chat.exception;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.exceptionClass.CustomException;

public class ChatException extends CustomException {

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChatException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
