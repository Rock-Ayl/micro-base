package com.rock.micro.base.common.api;

import com.rock.micro.base.util.LambdaParseFieldNameExtraUtils;
import lombok.Getter;

/**
 * 系统统一封装,运行时异常
 */
@Getter
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    //非通用-开发内部-中文描述
    public String zhDesc;

    //通用-对用户-国际化异常枚举
    public MyExceptionEnum myExceptionEnum;

    //通用-对用户-抛出时返回错误的key
    public String errorKey;

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
     * 通用-抛给用户,需要做国际化的异常(如密码格式、邮箱格式等错误),额外返回错误key
     *
     * @param myExceptionEnum 异常枚举
     * @param errorKey        错误key
     */
    public <T, R> MyException(MyExceptionEnum myExceptionEnum, LambdaParseFieldNameExtraUtils.MFunction<T, R> errorKey) {
        this(myExceptionEnum);
        this.errorKey = LambdaParseFieldNameExtraUtils.getColumn(errorKey);
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
