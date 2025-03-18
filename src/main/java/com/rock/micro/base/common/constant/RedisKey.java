package com.rock.micro.base.common.constant;

/**
 * Redis所有key存放在这里
 *
 * @Author ayl
 * @Date 2022-03-16
 */
public class RedisKey {

    /**
     * 全称key(也就是说不需要组装后缀的那种)
     */

    //demo mq 
    public final static String DEMO_MQ_ONE = "DEMO:MQ:ONE";

    /**
     * 非全称key(key本身不完整,后面需要如用户id等其他value的key)
     */

    //用户登录信息缓存
    public final static String USER_LOGIN_AUTH_SET = "USER:LOGIN:AUTH_SET:";

}
