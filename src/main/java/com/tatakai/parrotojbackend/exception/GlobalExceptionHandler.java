package com.tatakai.parrotojbackend.exception;


import com.tatakai.parrotojbackend.result.BaseResponse;
import com.tatakai.parrotojbackend.result.ErrorCode;
import com.tatakai.parrotojbackend.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(BusinessException e){
        log.error(e.getMessage()+"\n"+ Arrays.toString(e.getStackTrace()));
        return ResultUtil.failure(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> handleRuntimeException(RuntimeException e){
        log.error(e.getMessage()+"\n"+ Arrays.toString(e.getStackTrace()));
        return ResultUtil.failure(ErrorCode.SYSTEM_ERROR.getCode(),e.getCause().toString());
    }
}
