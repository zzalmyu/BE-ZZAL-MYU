package com.prgrms.zzalmyu.domain.report.exception;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.exception.exceptionClass.CustomException;

public class ReportException extends CustomException {

    public ReportException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReportException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
