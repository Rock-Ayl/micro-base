package com.rock.micro.base.common.mongo.config.convert;

import com.rock.micro.base.common.mongo.config.convert.bigDecimal.BigDecimalToDecimal128Converter;
import com.rock.micro.base.common.mongo.config.convert.bigDecimal.Decimal128ToBigDecimalConverter;
import com.rock.micro.base.common.mongo.config.convert.bigDecimal.Decimal128ToObjectConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * mongo 读写转换配置类
 *
 * @Author ayl
 * @Date 2023-04-21
 */
@Configuration
public class MongoConvertConfig {

    /**
     * mongoCustomConversions会由spring进行管理,
     * 按照加入的转换器,在数据库读写时对数据类型进行转换
     *
     * @return
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {

        //初始化转换器
        List<Converter<?, ?>> converterList = new ArrayList<>();

        /**
         * {@link java.math.BigDecimal 的转换兼容}
         */

        converterList.add(new BigDecimalToDecimal128Converter());
        converterList.add(new Decimal128ToBigDecimalConverter());
        converterList.add(new Decimal128ToObjectConverter());

        return new MongoCustomConversions(converterList);
    }

}