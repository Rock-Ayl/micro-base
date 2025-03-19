package com.rock.micro.base.common.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统统一封装,运行时异常
 */
@Getter
@Setter
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    //统一编码
    public Integer myExtCode;

    /**
     * 初始化
     *
     * @param myExtCode 错误编码
     * @param message   消息体
     */
    public MyException(Integer myExtCode, String message) {
        super(message);
        this.myExtCode = myExtCode;
    }

}
