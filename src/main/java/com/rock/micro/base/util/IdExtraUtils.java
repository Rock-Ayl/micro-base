package com.rock.micro.base.util;

import java.util.UUID;

/**
 * ID 扩展工具包
 *
 * @Author ayl
 * @Date 2022-03-10
 */
public class IdExtraUtils {

    /**
     * 生成无连字符的 UUID
     *
     * @return
     */
    public static String genGUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
