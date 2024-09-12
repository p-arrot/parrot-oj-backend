package com.example.parrot.exception;

import com.example.parrot.result.ErrorCode;

public class BusinessException extends RuntimeException {
    private final int code;

    private BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
    public BusinessException(ErrorCode errorCode,String msg) {
        this(errorCode.getCode(), msg);
    }

    public int getCode() {
        return code;
    }
}
