package com.rock.micro.base.util;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
     * 时间 转 string,只有数字,每日维度
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
     * 时间 转 string,只有数字,每分钟维度
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
     * 时间 转 string,只有数字,每小时维度
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
     * 时间 转 string,只有数字,每秒维度
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
     * 时间 转 string,只有数字,每半小时维度
     *
     * @param date 时间
     * @return
     */
    public static String dateToYmdHmStringNumberHalfHour(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //取整半小时
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minute = cal.get(Calendar.MINUTE);
        cal.set(Calendar.MINUTE, minute < 30 ? 0 : 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //返回
        return new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
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

    /**
     * 中国时间 转 美国时间,并格式化
     *
     * @param date 时间
     * @return
     */
    public static String dateToUSYmdString(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //太平洋标准时间(utc-8)
        ZoneId usZone = ZoneId.of("America/Los_Angeles");
        //转化并格式化
        return date.toInstant().atZone(usZone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
    }

    /**
     * 中国时间 转 美国时间,并格式化
     *
     * @param date 时间
     * @return
     */
    public static String dateToUSMmmDYyyyString(Date date) {
        //判空
        if (date == null) {
            //过
            return null;
        }
        //太平洋标准时间(utc-8)
        ZoneId usZone = ZoneId.of("America/Los_Angeles");
        //转化并格式化
        return date.toInstant().atZone(usZone).format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US));
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