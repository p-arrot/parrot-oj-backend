package com.tatakai.parrotojbackend.model.dto;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String userAccount;
    private String userPassword;

}
