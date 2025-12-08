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

    @ApiModelProperty("静态-服务环境枚举")
    public static ServiceEnvironmentEnum STATIC_SERVICE_ENVIRONMENT;

    @ApiModelProperty("静态-端口")
    public static Integer STATIC_PORT;

    @ApiModelProperty("静态-IP")
    public static String STATIC_IP;

    /**
     * 配置文件
     */

    @ApiModelProperty("服务-名称")
    @Value("${spring.application.name}")
    private String serviceName;

    @ApiModelProperty("服务-端口")
    @Value("${server.port}")
    private Integer port;

    @ApiModelProperty("服务-环境编码")
    @Value("${micro.rock.mine.service.environment}")
    private String serviceEnvironment;

    @PostConstruct
    private void init() {

        /**
         * 载入配置参数
         */

        STATIC_SERVICE_NAME = this.serviceName;
        STATIC_PORT = this.port;
        STATIC_SERVICE_ENVIRONMENT = ServiceEnvironmentEnum.parseByCode(this.serviceEnvironment);
        try {
            STATIC_IP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }

        /**
         * 通过生产、测试处理参数
         */

        //根据环境配置
        switch (STATIC_SERVICE_ENVIRONMENT) {
            //测试
            case TEST:
                break;
            //预发
            case PRE_VIEW:
                break;
            //生产
            case ONLINE:
                break;
        }

    }

}