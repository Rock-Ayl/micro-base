package com.rock.micro.base.common.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 服务环境枚举
 */
@Getter
public enum ServiceEnvironmentEnum {

    TEST("test", "测试-内网"),
    PRE_VIEW("preview", "线上-预发"),
    ONLINE("online", "线上-外网"),

    ;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("描述")
    private String desc;

    ServiceEnvironmentEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static ServiceEnvironmentEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (ServiceEnvironmentEnum object : ServiceEnvironmentEnum.values()) {
                //如果一致
                if (object.getCode().equals(code)) {
                    //返回
                    return object;
                }
            }
        }
        //默认测试环境
        return TEST;
    }

}
