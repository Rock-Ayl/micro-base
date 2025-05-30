package com.rock.micro.base.util;

import com.rock.micro.base.common.api.MyException;
import com.rock.micro.base.common.api.MyExceptionEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 通用断言类
 */
public class AssertExtraUtils {

    /**
     * 一定抛出
     *
     * @param zhDesc 中文描述
     */
    public static void assertFail(String zhDesc) {
        //抛出
        throw new MyException(zhDesc);
    }

    /**
     * 坚持为 true
     *
     * @param bool   传入参数
     * @param zhDesc 中文描述
     */
    public static void assertTrue(boolean bool, String zhDesc) {
        //判断
        if (bool == false) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 坚持为 false
     *
     * @param bool   传入参数
     * @param zhDesc 中文描述
     */
    public static void assertFalse(boolean bool, String zhDesc) {
        //判断
        if (bool == true) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 坚持不为 null
     *
     * @param obj    传入参数
     * @param zhDesc 中文描述
     */
    public static void assertNotNull(Object obj, String zhDesc) {
        //判断
        if (obj == null) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 坚持不为 Empty
     *
     * @param obj    传入参数
     * @param zhDesc 中文描述
     */
    public static void assertNotEmpty(String obj, String zhDesc) {
        //判断
        if (StringUtils.isEmpty(obj)) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 坚持不为 Blank
     *
     * @param obj    传入参数
     * @param zhDesc 中文描述
     */
    public static void assertNotBlank(String obj, String zhDesc) {
        //判断
        if (StringUtils.isBlank(obj)) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 坚持不为  Empty
     *
     * @param collection 集合
     * @param zhDesc     中文描述
     */
    public static void assertNotEmpty(Collection collection, String zhDesc) {
        //判空
        if (CollectionUtils.isEmpty(collection) || collection.size() < 1) {
            //抛出
            throw new MyException(zhDesc);
        }
    }

    /**
     * 一定抛出
     *
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertFail(MyExceptionEnum myExceptionEnum) {
        //抛出
        throw new MyException(myExceptionEnum);
    }

    /**
     * 坚持为 true
     *
     * @param bool            传入参数
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertTrue(boolean bool, MyExceptionEnum myExceptionEnum) {
        //判断
        if (bool == false) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 坚持为 false
     *
     * @param bool            传入参数
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertFalse(boolean bool, MyExceptionEnum myExceptionEnum) {
        //判断
        if (bool == true) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 坚持不为 null
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertNotNull(Object obj, MyExceptionEnum myExceptionEnum) {
        //判断
        if (obj == null) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 坚持不为 Empty
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertNotEmpty(String obj, MyExceptionEnum myExceptionEnum) {
        //判断
        if (StringUtils.isEmpty(obj)) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 坚持不为 Blank
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertNotBlank(String obj, MyExceptionEnum myExceptionEnum) {
        //判断
        if (StringUtils.isBlank(obj)) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 坚持不为 Empty
     *
     * @param collection      集合
     * @param myExceptionEnum 要抛出的枚举
     */
    public static void assertNotEmpty(Collection collection, MyExceptionEnum myExceptionEnum) {
        //判空
        if (CollectionUtils.isEmpty(collection) || collection.size() < 1) {
            //抛出
            throw new MyException(myExceptionEnum);
        }
    }

    /**
     * 一定抛出
     *
     * @param myExceptionEnum 要抛出的枚举
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertFail(MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //抛出
        throw new MyException(myExceptionEnum, errorKey);
    }

    /**
     * 坚持为 true
     *
     * @param bool            传入参数
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertTrue(boolean bool, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判断
        if (bool == false) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

    /**
     * 坚持为 false
     *
     * @param bool            传入参数
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertFalse(boolean bool, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判断
        if (bool == true) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

    /**
     * 坚持不为 null
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertNotNull(Object obj, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判断
        if (obj == null) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

    /**
     * 坚持不为 Empty
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertNotEmpty(String obj, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判断
        if (StringUtils.isEmpty(obj)) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

    /**
     * 坚持不为 Blank
     *
     * @param obj             传入参数
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertNotBlank(String obj, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判断
        if (StringUtils.isBlank(obj)) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

    /**
     * 坚持不为 Empty
     *
     * @param collection      集合
     * @param myExceptionEnum 要抛出的枚举
     * @param errorKey        错误key
     */
    public static <T, R> void assertNotEmpty(Collection collection, MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        //判空
        if (CollectionUtils.isEmpty(collection) || collection.size() < 1) {
            //抛出
            throw new MyException(myExceptionEnum, errorKey);
        }
    }

}