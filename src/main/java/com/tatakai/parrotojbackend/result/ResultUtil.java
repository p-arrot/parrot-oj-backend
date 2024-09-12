package com.example.parrot.result;

import com.fasterxml.jackson.databind.ser.Serializers;

public class ResultUtil {
    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data);
    }

    public static BaseResponse<?> failure(int code,String msg){
        return new BaseResponse<>(code,msg);
    }

    public static BaseResponse<?> failure(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }


}
