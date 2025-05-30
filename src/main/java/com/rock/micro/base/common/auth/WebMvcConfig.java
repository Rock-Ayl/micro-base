package com.rock.micro.base.common.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 请求拦截器配置
 *
 * @Author ayl
 * @Date 2022-03-21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //装载拦截器
        registry.addInterceptor(loginInterceptor());
    }

    @Bean
    public ControllerInterceptor loginInterceptor() {
        return new ControllerInterceptor();
    }

    /**
     * 配置跨域,本项目在 nginx 配置了,所以这里不配置
     */

}
