package com.rock.micro.base.util;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 时间 补充工具包
 *
 * @Author ayl
 * @Date 2024-01-19
 */
public class DateExtraUtils {

    /**
     * 将时间转化为string 格式为标准:w3 dateTime (地址 https://www.w3.org/TR/NOTE-datetime)
     *
     * @param date
     * @return
     */
    public static String toW3DateTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date) + "T" + new SimpleDateFormat("HH:mm:ss").format(date) + "+08:00";
    }

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

    /**
     * 指定时间区间 随机一个日期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static Date randomDate(Date start, Date end) {
        long startMillis = start.getTime();
        long endMillis = end.getTime();
        long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return new Date(randomMillis);
    }

    public static void main(String[] args) {
        Date start = DateUtil.parse("2025-01-01");
        Date end = DateUtil.parse("2025-12-31");
        for (int i = 0; i < 100; i++) {
            Date random = randomDate(start, end);
            System.out.println("随机日期：" + DateUtil.formatDateTime(random));
        }
    }

}