package com.rock.micro.base.common.enums;

import lombok.Getter;

/**
 * 常用http状态码枚举(需要就慢慢维护,没必要维护太多不常用的)
 * 参考地址:https://www.runoob.com/servlet/servlet-http-status-codes.html
 *
 * @Author ayl
 * @Date 2022-03-22
 */
@Getter
public enum HttpStatusEnum {

    NONE(0, "未知", "不存在或不识别的状态码"),

    OK(200, "OK", "请求成功"),

    BAD_REQUEST(400, "Bad Request", "服务器不理解请求,或接口参数有误"),
    UNAUTHORIZED(401, "Unauthorized", "该请求需要身份验证,验证失败."),
    FORBIDDEN(403, "Forbidden", "该请求被禁止访问"),
    NOT_FOUND(404, "Not Found", "无法找到该请求的内容"),

    ;

    //代码
    private Integer code;

    //消息
    private String message;

    //描述
    private String desc;

    HttpStatusEnum(Integer code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

}
