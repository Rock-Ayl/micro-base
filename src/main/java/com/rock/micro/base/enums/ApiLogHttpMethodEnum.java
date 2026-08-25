package com.rock.micro.base.enums;

import lombok.Getter;

/**
 * 接口日志-HTTP请求类型 枚举
 */
@Getter
public enum ApiLogHttpMethodEnum {

    NONE("NONE", "未知"),

    GET("GET", "GET请求"),
    POST("POST", "POST请求"),
    PUT("PUT", "PUT请求"),
    PATCH("PATCH", "PATCH请求"),
    DELETE("DELETE", "DELETE请求"),
    HEAD("HEAD", "HEAD请求"),
    OPTIONS("OPTIONS", "OPTIONS请求"),
    TRACE("TRACE", "TRACE请求"),

    ;

    //编码
    private String code;

    //描述
    private String desc;

    ApiLogHttpMethodEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static ApiLogHttpMethodEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (ApiLogHttpMethodEnum object : ApiLogHttpMethodEnum.values()) {
                //如果一致
                if (object.getCode().equalsIgnoreCase(code)) {
                    //返回
                    return object;
                }
            }
        }
        //默认未知
        return NONE;
    }

}

