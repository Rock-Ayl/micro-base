package com.rock.micro.base.common.auth;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务进程配置
 *
 * @Author ayl
 * @Date 2025-03-27
 */
@Component
public class ServiceInfo {

    /**
     * 静态字段
     */

    @ApiModelProperty("静态-服务名")
    public static String STATIC_SERVICE_NAME;

    @ApiModelProperty("静态-端口")
    public static Integer STATIC_PORT;

    @ApiModelProperty("静态-IP")
    public static String STATIC_IP;

    /**
     * 配置文件
     */

    @ApiModelProperty("服务名")
    @Value("${spring.application.name}")
    private String serviceName;

    @ApiModelProperty("端口")
    @Value("${server.port}")
    private Integer port;

    @PostConstruct
    private void init() {
        STATIC_SERVICE_NAME = this.serviceName;
        STATIC_PORT = this.port;
        try {
            STATIC_IP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }
    }

    /**
     * 获取是否为 内网-测试环境
     *
     * @return
     */
    public static boolean internalTestingEnvironment() {
        //实现
        return ServiceInfo.STATIC_IP.startsWith("192.168") == true;
    }

}