package com.example.parrot.aop;

import com.example.parrot.annotation.AuthCheck;
import com.example.parrot.exception.BusinessException;
import com.example.parrot.model.entity.User;
import com.example.parrot.model.enums.AuthEnum;
import com.example.parrot.result.ErrorCode;
import com.example.parrot.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class AuthCheckInterceptor{
    @Resource
    private UserService userService;
    @Around("@annotation(authCheck)")
    public Object checkRole(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        AuthEnum mustRole = authCheck.mustRole();
        if(mustRole == null || mustRole.equals(AuthEnum.NOT_LOGIN)){
            return joinPoint.proceed();
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        User loginUser = userService.getLoginUser(request);
        AuthEnum userRole = AuthEnum.getEnumByValue(loginUser.getUserRole());
        if(userRole==null || AuthEnum.NOT_LOGIN.equals(userRole)){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        if (AuthEnum.ADMIN.equals(mustRole)){
            if(AuthEnum.ADMIN.equals(userRole)){
                return joinPoint.proceed();
            }else {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"仅管理员可访问！");
            }
        }
        return joinPoint.proceed();
    }
}
