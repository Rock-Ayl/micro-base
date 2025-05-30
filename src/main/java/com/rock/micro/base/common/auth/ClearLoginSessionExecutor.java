package com.rock.micro.base.common.auth;

/**
 * 清理 {@link LoginAuth} 的所有内容
 */
public class ClearLoginSessionExecutor {

    /**
     * 实现
     */
    public static void clear() {
        LoginAuth.USER.remove();
        LoginAuth.IP.remove();
    }

}