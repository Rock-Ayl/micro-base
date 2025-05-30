package com.rock.micro.base.common.rocketmq;

import io.swagger.annotations.ApiModelProperty;

/**
 * 全局服务唯一的 RocketMQ 常量 配置
 *
 * @Author ayl
 * @Date 2025-05-30
 */
public class RocketMQConstant {

    /**
     * 主题
     */

    @ApiModelProperty("主题[通用主题]")
    public static final String TOPIC_NORMAL = "NORMAL";

    /**
     * 生产者分组 , 格式 = [PRODUCER_GROUP_服务]
     */

    @ApiModelProperty("生产者分组：用户服务")
    public static final String PRODUCER_GROUP_USER = "PRODUCER_GROUP_USER";

    @ApiModelProperty("生产者分组：通用服务")
    public static final String PRODUCER_GROUP_COMMON = "PRODUCER_GROUP_COMMON";

    /**
     * 消费者分组 , 格式前缀 = [CONSUMER_GROUP_服务_主题_其他]
     */

    @ApiModelProperty("消费者分组：normal主题-用户")
    public static final String CONSUMER_GROUP_USER_NORMAL = "CONSUMER_GROUP_USER_NORMAL";

    @ApiModelProperty("消费者分组：normal主题-通用")
    public static final String CONSUMER_GROUP_COMMON_NORMAL = "CONSUMER_GROUP_COMMON_NORMAL";

    /**
     * 标签
     */

    @ApiModelProperty("标签：所有")
    public static final String TAG_ALL = "*";

    @ApiModelProperty("标签：测试1")
    public static final String TAG_TEST_1 = "TEST-1";


}