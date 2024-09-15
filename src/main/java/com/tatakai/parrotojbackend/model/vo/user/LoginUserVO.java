package com.tatakai.parrotojbackend.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO {


    private String userAccount;
    private String userName;
    private String userRole;


}
