package com.tatakai.parrotojbackend;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeRequest;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;
import com.tatakai.parrotojbackend.result.ErrorCode;
import com.tatakai.parrotojbackend.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class ParrotOjBackendApplicationTests {
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Test
    void contextLoads() {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(List.of("1","2"));
        executeCodeRequest.setCode("");
        executeCodeRequest.setLanguage("java");

        log.info("调用远程代码沙箱。。。。");
        String url = "http://111.229.65.33:8081/executeCode";
        HttpResponse response = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(JSONUtil.toJsonStr(executeCodeRequest)).execute();
        String body = response.body();
        ThrowUtil.throwIf(StringUtils.isEmpty(body), ErrorCode.OPERATION_ERROR, "远程代码沙箱执行失败");
        JSONUtil.toBean(body, ExecuteCodeResponse.class);
    }


}
