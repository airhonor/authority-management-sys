package com.hz.authority.management.sys.exception;

import com.hz.authority.management.sys.constant.ResultCode;
import lombok.Getter;

/**
 * @author honorzhang
 */
public class PermissionDeniedException extends RuntimeException {

    @Getter
    private int code;

    public PermissionDeniedException(String message) {
        super(message);
        this.code = ResultCode.UN_AUTHORIZED.getCode();
    }

    public PermissionDeniedException(String message, ResultCode resultCode) {
        super(message);
        this.code = resultCode.getCode();
    }

    public PermissionDeniedException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public PermissionDeniedException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.code = resultCode.getCode();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
