package com.tatakai.parrotojbackend.codeSandbox;

import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeRequest;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
