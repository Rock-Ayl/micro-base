package com.rock.micro.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间 补充工具包
 *
 * @Author ayl
 * @Date 2024-01-19
 */
public class DateExtraUtils {

    /**
     * 时间 转 string,只有数字
     *
     * @param date 时间
     * @return
     */
    public static String dateToYmdStringNumber(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //非线程安全,所以new实现
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 时间 转 string,只有数字
     *
     * @param date 时间
     * @return
     */
    public static String dateToYmdHmStringNumber(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //非线程安全,所以new实现
        return new SimpleDateFormat("yyyyMMddHHmm").format(date);
    }

    /**
     * 时间 转 string,只有数字
     *
     * @param date 时间
     * @return
     */
    public static String dateToYmdHStringNumber(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //非线程安全,所以new实现
        return new SimpleDateFormat("yyyyMMddHH").format(date);
    }

    /**
     * 时间 转 string,只有数字
     *
     * @param date 时间
     * @return
     */
    public static String dateToYmdHmsStringNumber(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //非线程安全,所以new实现
        return new SimpleDateFormat("yyMMddHHmmss").format(date);
    }

}
