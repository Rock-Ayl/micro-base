package com.rock.micro.base.common.api;

import com.rock.micro.base.util.LambdaParseFieldNameExtraUtils;
import lombok.Getter;

import java.util.List;

/**
 * 系统统一封装,运行时异常
 */
@Getter
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    //非通用-开发内部-中文描述
    private String zhDesc;

    //通用-对用户-国际化异常枚举
    private MyExceptionEnum myExceptionEnum;

    //通用-对用户-抛出时返回错误的 key
    private String errorKey;

    //通用-对用户-抛出时返回错误的 值列表
    private List<String> errorValueList;

    /**
     * 通用-抛给用户,需要做国际化的异常(如密码格式、邮箱格式等错误)
     *
     * @param myExceptionEnum 异常枚举
     */
    public MyException(MyExceptionEnum myExceptionEnum) {
        //message用中文
        super(myExceptionEnum.getZhDesc());
        //记录枚举
        this.myExceptionEnum = myExceptionEnum;
    }

    /**
     * 通用-抛给用户,需要做国际化的异常(如密码格式、邮箱格式等错误),额外返回错误 key
     *
     * @param myExceptionEnum 异常枚举
     * @param errorKey        错误key
     */
    public <T, R> MyException(MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        this(myExceptionEnum);
        this.errorKey = LambdaParseFieldNameExtraUtils.getColumn(errorKey);
    }

    /**
     * 通用-抛给用户,需要做国际化的异常(如密码格式、邮箱格式等错误),额外返回错误 value
     *
     * @param myExceptionEnum 异常枚举
     * @param errorValueList  错误的值列表
     */
    public <T, R> MyException(MyExceptionEnum myExceptionEnum, List<String> errorValueList) {
        this(myExceptionEnum);
        this.errorValueList = errorValueList;
    }

    /**
     * 通用-抛给用户,需要做国际化的异常(如密码格式、邮箱格式等错误),额外返回错误 key + value
     *
     * @param myExceptionEnum 异常枚举
     * @param errorKey        错误key
     * @param errorValueList  错误的值列表
     */
    public <T, R> MyException(MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey, List<String> errorValueList) {
        this(myExceptionEnum);
        this.errorKey = LambdaParseFieldNameExtraUtils.getColumn(errorKey);
        this.errorValueList = errorValueList;
    }

    /**
     * 非通用-抛给开发人员,用户不可见的异常,用来快速定位错误
     *
     * @param zhDesc 中文描述
     */
    public MyException(String zhDesc) {
        //message用中文
        super(zhDesc);
        //中文描述
        this.zhDesc = zhDesc;
    }

}