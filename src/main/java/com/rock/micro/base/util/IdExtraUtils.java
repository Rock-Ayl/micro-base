package com.rock.micro.base.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.rock.micro.base.common.auth.ServiceInfo;

/**
 * ID 扩展工具包
 *
 * @Author ayl
 * @Date 2022-03-10
 */
public class IdExtraUtils {

    //静态算法实例
    private static final Snowflake INSTANCE;

    /**
     * 生成算法实例
     */
    static {
        //获取本机ip
        String ipHost = NetUtil.getLocalhostStr();
        //获取本机端口
        int port = ServiceInfo.STATIC_PORT != null ? ServiceInfo.STATIC_PORT : 0;
        //工作节点id生成
        long workerId = (NetUtil.ipv4ToLong(ipHost) + port) % 32L;
        //多数据中心时需调整(机房)
        int dataCenterId = 1;
        //生成对应算法实例
        INSTANCE = IdUtil.getSnowflake(workerId, dataCenterId);
    }

    /**
     * 获取全局唯一id
     *
     * @return
     */
    public static String genGUID() {
        return INSTANCE.nextIdStr();
    }

}
