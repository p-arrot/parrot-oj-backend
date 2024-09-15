package com.tatakai.parrotojbackend.utils;

import com.tatakai.parrotojbackend.exception.BusinessException;
import com.tatakai.parrotojbackend.result.ErrorCode;

public class ThrowUtil {
    public static void throwIf(boolean flag, ErrorCode errorCode) {
        if(flag) throw new BusinessException(errorCode);
    }
    public static void throwIf(boolean flag, ErrorCode errorCode,String message) {
        if(flag) throw new BusinessException(errorCode,message);
    }
}
