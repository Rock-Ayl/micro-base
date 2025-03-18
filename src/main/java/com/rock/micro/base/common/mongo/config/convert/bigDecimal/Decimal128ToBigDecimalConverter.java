package com.rock.micro.base.common.mongo.config.convert.bigDecimal;


import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;

/**
 * Decimal128 -> BigDecimal 读取转换器
 *
 * @Author ayl
 * @Date 2023-04-21
 */
@ReadingConverter
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    @Override
    public BigDecimal convert(Decimal128 decimal128) {
        return decimal128.bigDecimalValue();
    }

}
