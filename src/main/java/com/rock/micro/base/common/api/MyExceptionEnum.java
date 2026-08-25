package com.rock.micro.base.common.api;

import com.alibaba.fastjson.JSONObject;
import com.rock.micro.base.common.constant.JSONConst;
import lombok.Getter;

/**
 * 常用http状态码枚举(需要就慢慢维护,没必要维护太多不常用的)
 * 参考地址:https://www.runoob.com/servlet/servlet-http-status-codes.html
 *
 * @Author ayl
 * @Date 2022-03-22
 */
@Getter
public enum MyExceptionEnum {

    /**
     * 默认
     */

    NORMAL_ERROR(50000, "系统开小差了，请稍后再试", "Something went wrong，please refresh the page or try again later."),
    NORMAL_MUST_ROOT(50001, "您暂时无法访问此内容", "You don’t have permission to access this page."),
    NORMAL_NO_PERMISSION(50002, "您暂无权限访问该资源", "You do not have permission to access this resource."),
    SYSTEM_MAINTENANCE(50003, "系统正在维护中，请稍后再试", "The system is currently undergoing maintenance. Please try again later."),

    /**
     * 操作
     */

    EXCEEDED_RETRY_ATTEMPTS(60000, "操作太快了，请稍等一下再试", "You’re doing that a bit too fast. Please try again shortly"),
    OPERATION_DO_NOT_REPEAT(60005, "该操作正在处理中，请稍等", "This action is already in progress. Please wait a moment."),
    DATA_UPDATED_BY_OTHERS(60056, "操作的数据已被其他人更新，请刷新页面后重试", "The data has been updated by someone else. Please refresh the page and try again."),

    /**
     * 字符(character)
     */

    CHARACTER_ERROR(60500, "输入内容格式不正确，请检查后重新输入", "The input format is invalid. Please check and try again."),

    CANNOT_EXCEED_100_CHARACTER(60801, "请输入 1–100 个字符", "Please enter 1–100 characters."),

    CHARACTER_RANGE_6_20(61101, "请输入 6–20 个字符", "Please enter 6–20 characters."),

    /**
     * 数字、金额
     */

    NUMBER_RANGE_1_50000(60500, "请输入1-50000", "Please enter 1-50000 number"),

    MONEY_RANGE_001_999999(61000, "请输入0.01-999999金额,保留小数点后两位", "Please enter the amount of 0.01-999999, rounded to two decimal places"),

    /**
     * 参数
     */

    PARAM_ERROR(80500, "参数异常", "Param error"),
    PARAM_NULL_ERROR(80501, "参数为空", "Please enter your param"),
    PARAM_RULE_ERROR(80502, "参数不符合规则", "Please enter a valid param"),
    PARAM_NOT_FOUND(80503, "参数不存在", "Param not found"),

    /**
     * 手机号
     */

    NUMBER_ERROR(80700, "手机号异常", "Number error"),
    NUMBER_NULL_ERROR(80701, "手机号为空", "Please enter your number"),
    NUMBER_RULE_ERROR(80702, "手机号不符合规则", "Please enter a valid number"),
    NUMBER_NOT_FOUND(80703, "手机号不存在", "Number not found"),

    /**
     * 邮箱
     */

    EMAIL_ERROR(80900, "邮箱异常", "Email error"),
    EMAIL_NULL_ERROR(80901, "邮箱为空", "Please enter your email"),
    EMAIL_RULE_ERROR(80902, "邮箱不符合规则", "Please enter a valid email address"),
    EMAIL_NOT_FOUND(80903, "邮箱不存在", "Email not found"),

    EMAIL_NOT_REGISTERED(80910, "邮箱未注册", "Email not registered"),
    EMAIL_REGISTERED(80911, "邮箱已注册", "Email registered"),

    /**
     * 密码
     */

    PASSWORD_ERROR(81100, "密码异常", "Password error"),
    PASSWORD_NULL_ERROR(81101, "密码为空", "Please enter your password"),
    PASSWORD_RULE_ERROR(81102, "密码不符合规则", "Please enter a valid password"),
    PASSWORD_NOT_FOUND(81103, "密码不存在", "Password not found"),

    PASSWORD_INCORRECT(81110, "密码不正确", "Incorrect password"),

    /**
     * 验证码
     */

    VERIFICATION_CODE_ERROR(81300, "验证码异常", "VerificationCode error"),
    VERIFICATION_CODE_NULL_ERROR(81301, "验证码为空", "Please enter your verificationCode"),
    VERIFICATION_CODE_RULE_ERROR(81302, "验证码不符合规则", "Please enter a valid verificationCode"),
    VERIFICATION_CODE_RULE_NOT_FOUND(81303, "验证码不存在", "VerificationCode not found"),


    /**
     * 名称
     */

    NAME_ERROR(81500, "名称异常", "Name error"),
    NAME_NULL_ERROR(81501, "名称为空", "Please enter your name"),
    NAME_RULE_ERROR(81502, "名称不符合规则", "Please enter a valid name"),
    NAME_NOT_FOUND(81503, "名称不存在", "Name not found"),

    ;

    //代码
    private Integer code;

    //描述-中文
    private String zhDesc;

    //描述-英文
    private String enDesc;

    MyExceptionEnum(Integer code, String zhDesc, String enDesc) {
        this.code = code;
        this.zhDesc = zhDesc;
        this.enDesc = enDesc;
    }

    /**
     * 转为JSON
     *
     * @return
     */
    public JSONObject toJSON() {
        //转为json
        JSONObject json = new JSONObject();
        //组装
        json.put(JSONConst.KEY_CODE, this.getCode());
        json.put(JSONConst.KEY_ZH_DESC, this.getZhDesc());
        json.put(JSONConst.KEY_EN_DESC, this.getEnDesc());
        //返回
        return json;
    }

}
