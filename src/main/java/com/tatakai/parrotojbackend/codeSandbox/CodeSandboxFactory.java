package com.tatakai.parrotojbackend.codeSandbox;

import com.tatakai.parrotojbackend.codeSandbox.impl.RemoteCodesandbox;

public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type){
        switch (type){
            case "remote":
                return new RemoteCodesandbox();
            default:
                return null;
        }

    }
}
