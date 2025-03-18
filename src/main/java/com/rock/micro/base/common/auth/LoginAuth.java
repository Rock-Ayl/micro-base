package com.rock.micro.base.common.auth;

import com.rock.micro.base.data.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录认证注解,控制层方法加上该注解,需要登录验证
 */
//运行期间依旧存在该注解
@Retention(RetentionPolicy.RUNTIME)
//指定可使用注解的位置:仅应用在方法
@Target(value = {ElementType.METHOD})
public @interface LoginAuth {

    //登录成功时,这里承载着该线程的用户信息
    ThreadLocal<User> USER = new ThreadLocal<>();

}