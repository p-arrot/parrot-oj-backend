package com.tatakai.parrotojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tatakai.parrotojbackend.exception.BusinessException;
import com.tatakai.parrotojbackend.mapper.UserMapper;
import com.tatakai.parrotojbackend.model.domain.User;
import com.tatakai.parrotojbackend.model.vo.LoginUserVO;
import com.tatakai.parrotojbackend.result.ErrorCode;
import com.tatakai.parrotojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 30215
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-09-05 23:11:
 *
 *
 *
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final String SALT = "CSL";
    private final String LOGIN_USER = "login_user";

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.allEq(Map.of("userAccount", userAccount, "userPassword", DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes())));
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("用户名或密码错误，登录失败");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误！");
        }
        request.getSession().setAttribute(LOGIN_USER, user);
        return getLoginUserVoByUser(user);
    }

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验用户名和密码的有效性
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码不得为空！");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致！");
        }
        if (userPassword.length() <4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码至少四位！");
        }
        // 2. 判断用户名是否已存在
        Long account = this.baseMapper.selectCount(new QueryWrapper<User>().eq("userAccount", userAccount));
        if(account>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名已存在！");
        }
        // 3. 添加用户
        synchronized (userAccount.intern()){
            String passwordInDb = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
            User user=new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(passwordInDb);
            boolean save = save(user);
            if(!save){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册用户失败！");
            }
            return user.getId();
        }


    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(LOGIN_USER);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute(LOGIN_USER);
        if(user==null) throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录！");
        request.getSession().removeAttribute(LOGIN_USER);
        return true;
    }


    public LoginUserVO getLoginUserVoByUser(User user) {
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }
}




