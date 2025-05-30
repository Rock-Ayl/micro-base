package com.rock.micro.base.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.json.JsonParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Jackson 扩展工具包
 * -
 * 1. 默认 按照Java语言的规定,输出eg: productName -> productName
 * 2. 可选 按照C语言的规定,输出eg: productName -> product_name
 *
 * @Author ayl
 * @Date 2024-01-25
 */
public class JacksonExtraUtils {

    //默认 ObjectMapper 小驼峰单例
    private final static ObjectMapper DEFAULT_OBJECT_MAPPER;
    //可选 ObjectMapper 下划线单例
    private final static ObjectMapper SNAKE_CASE_OBJECT_MAPPER;
    //可选 ObjectMapper 大驼峰单例
    private final static ObjectMapper UPPER_CAMEL_CASE_OBJECT_MAPPER;

    static {

        /**
         * 默认 {@link ObjectMapper}
         * -
         * Java风格-小驼峰
         */

        //初始化 默认 ObjectMapper
        DEFAULT_OBJECT_MAPPER = initMapper();

        /**
         * 可选 {@link ObjectMapper}
         * -
         * C风格-下划线
         */

        //初始化 默认 ObjectMapper
        SNAKE_CASE_OBJECT_MAPPER = initMapper();
        //设置字段命名策略为下划线格式
        SNAKE_CASE_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        /**
         * 可选 {@link ObjectMapper}
         * -
         * 通用-大驼峰
         */

        //初始化 默认 ObjectMapper
        UPPER_CAMEL_CASE_OBJECT_MAPPER = initMapper();
        //设置字段命名策略为大驼峰
        UPPER_CAMEL_CASE_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

    }

    /**
     * 初始化一个绝对通用的 {@link ObjectMapper}
     *
     * @return
     */
    private static ObjectMapper initMapper() {

        //初始化
        ObjectMapper objectMapper = new ObjectMapper();

        //启用JSON缩进美化输出(可选) objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        /**
         * {@link String} -> {@link Object} 配置
         */

        //如果string有某个key但实体没有,正常会抛异常,但这么配置会忽略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果没有任何属性,正常会抛出,但这么配置会忽略
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        /**
         * {@link Object} -> {@link String} 配置
         */

        //如果对象key的值为空,正常会输出null,但这么配置会忽略
        //前端要求统一 objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        //返回
        return objectMapper;
    }

    /**
     * 获取 默认 {@link ObjectMapper}
     * -
     * Java规则-小驼峰格式
     *
     * @return
     */
    private static ObjectMapper getDefaultMapper() {
        //返回
        return DEFAULT_OBJECT_MAPPER;
    }

    /**
     * 获取 下划线风格 {@link ObjectMapper}
     * -
     * Java规则-小驼峰格式
     *
     * @return
     */
    private static ObjectMapper getSnakeCaseMapper() {
        //返回
        return SNAKE_CASE_OBJECT_MAPPER;
    }

    /**
     * 获取 大驼峰风格 {@link ObjectMapper}
     * -
     * 通用-大驼峰格式
     *
     * @return
     */
    private static ObjectMapper getUpperCamelCaseObjectMapper() {
        //返回
        return UPPER_CAMEL_CASE_OBJECT_MAPPER;
    }

    /**
     * jackson 统一实现
     *
     * @param parser
     * @param <T>
     * @return
     */
    private static <T> T tryParse(Callable<T> parser) {
        try {
            return parser.call();
        } catch (Exception e) {
            //如果是 jackson 的异常
            if (JacksonException.class.isAssignableFrom(e.getClass())) {
                //抛出spring的
                throw new JsonParseException(e);
            }
            //默认
            throw new IllegalStateException(e);
        }
    }

    /**
     * 对象转String {@link Object} -> {@link String}
     * -
     * java风格-小驼峰
     *
     * @param object 对象
     * @return
     */
    public static String toJSONString(Object object) {
        //使用默认 mapper 实现
        return toJSONString(getDefaultMapper(), object);
    }

    /**
     * 对象转String {@link Object} -> {@link String}
     * -
     * C风格-下划线格式
     *
     * @param object 对象
     * @return
     */
    public static String toSnakeCaseJSONString(Object object) {
        //选定 mapper 实现
        return toJSONString(getSnakeCaseMapper(), object);
    }

    /**
     * 对象转String {@link Object} -> {@link String}
     * -
     * 通用-大驼峰
     *
     * @param object 对象
     * @return
     */
    public static String toUpperCamelCaseJSONString(Object object) {
        //选定 mapper 实现
        return toJSONString(getUpperCamelCaseObjectMapper(), object);
    }

    /**
     * 对象转String {@link Object} -> {@link String}
     *
     * @param objectMapper 对应 mapper
     * @param object       对象
     * @return
     */
    private static String toJSONString(ObjectMapper objectMapper, Object object) {
        //判空
        if (object == null || objectMapper == null) {
            //过
            return null;
        }
        //如果就是string
        if (object instanceof String) {
            //直接返回,无需再转
            return object.toString();
        }
        //实现
        return tryParse(() -> objectMapper.writeValueAsString(object));
    }

    /**
     * 深克隆单个对象,也可以将一个对象转化为另一个对象(当然,结构得基本一致或继承关系)
     *
     * @param object       源对象,比如父对象,不能是数组等结构
     * @param toJavaObject 目标对象
     * @return
     */
    public static <T> T deepClone(Object object, Class<T> toJavaObject) {
        //默认mapper实现
        return deepClone(getDefaultMapper(), object, toJavaObject);
    }

    /**
     * 深克隆单个对象,也可以将一个对象转化为另一个对象(当然,结构得基本一致或继承关系)
     *
     * @param objectMapper 对应 mapper
     * @param object       源对象,比如父对象,不能是数组等结构
     * @param toJavaObject 目标对象
     * @return
     */
    private static <T> T deepClone(ObjectMapper objectMapper, Object object, Class<T> toJavaObject) {
        //判空
        if (object == null) {
            //过
            return null;
        }
        //先转为string,再转为对应实体
        return tryParse(() -> objectMapper.readValue(toJSONString(object), toJavaObject));
    }

    /**
     * 深克隆单个对象,也可以将一个对象转化为另一个对象(当然,结构得基本一致或继承关系)
     *
     * @param object       源对象,比如父对象,不能是数组等结构
     * @param toJavaObject 目标对象
     * @return
     */
    public static <T> List<T> deepCloneList(Object object, Class<T> toJavaObject) {
        //默认mapper实现
        return deepCloneList(getDefaultMapper(), object, toJavaObject);
    }

    /**
     * 深克隆单个对象,也可以将一个对象转化为另一个对象(当然,结构得基本一致或继承关系)
     *
     * @param objectMapper 对应 mapper
     * @param object       源对象,比如父对象,不能是数组等结构
     * @param toJavaObject 目标对象
     * @return
     */
    private static <T> List<T> deepCloneList(ObjectMapper objectMapper, Object object, Class<T> toJavaObject) {
        //判空
        if (object == null) {
            //过
            return null;
        }
        //先转为string,再转为对应实体
        return tryParse(() -> objectMapper.readValue(toJSONString(object), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, toJavaObject)));
    }

}