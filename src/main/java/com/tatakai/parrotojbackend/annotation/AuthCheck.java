package com.tatakai.parrotojbackend.annotation;

import com.tatakai.parrotojbackend.model.enums.AuthEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {
    AuthEnum mustRole() default AuthEnum.NOT_LOGIN;
}
