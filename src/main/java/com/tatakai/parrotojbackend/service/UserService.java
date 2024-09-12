package com.tatakai.parrotojbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tatakai.parrotojbackend.model.domain.User;
import com.tatakai.parrotojbackend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 30215
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-09-05 23:11:43
*/
public interface UserService extends IService<User> {
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);
    Long userRegister(String userAccount,String userPassword,String checkPassword);
    User getLoginUser(HttpServletRequest request);
    Boolean userLogout(HttpServletRequest request);

}
