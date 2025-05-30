package com.rock.micro.base.util;

import org.apache.commons.lang3.StringUtils;

/**
 * http 补充工具包
 *
 * @Author ayl
 * @Date 2025-05-20
 */
public class HttpExtraUtils {

    /**
     * 判断是否为https
     *
     * @param url 地址
     * @return
     */
    public static boolean https(String url) {
        //实现
        return StringUtils.isNotBlank(url) && url.startsWith("https://");
    }

}
