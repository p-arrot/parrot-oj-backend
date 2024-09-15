package com.tatakai.parrotojbackend.codeSandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.tatakai.parrotojbackend.codeSandbox.CodeSandbox;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeRequest;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;
import com.tatakai.parrotojbackend.result.ErrorCode;
import com.tatakai.parrotojbackend.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RemoteCodesandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("调用远程代码沙箱。。。。");
        String url = "http://111.229.65.33:8081/executeCode";
        HttpResponse response = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(JSONUtil.toJsonStr(executeCodeRequest)).execute();
        String body = response.body();
        ThrowUtil.throwIf(StringUtils.isEmpty(body), ErrorCode.OPERATION_ERROR, "远程代码沙箱执行失败");
        return JSONUtil.toBean(body, ExecuteCodeResponse.class);
    }
}
