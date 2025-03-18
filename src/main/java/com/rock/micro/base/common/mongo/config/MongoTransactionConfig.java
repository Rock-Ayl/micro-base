package com.rock.micro.base.common.mongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * mongodb 事务配置类
 *
 * @Author ayl
 * @Date 2024-12-18
 */
@Configuration
public class MongoTransactionConfig {

    /**
     * mongodb 事务配置Bean
     *
     * @param factory 工厂
     * @return 事务管理器
     */
    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager transactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }

}
