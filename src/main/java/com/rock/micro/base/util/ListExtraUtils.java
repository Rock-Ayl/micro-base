package com.rock.micro.base.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * List 扩展工具包
 *
 * @Author ayl
 * @Date 2022-3-11
 */
public class ListExtraUtils {

    /**
     * 根据逗号拆分str并过滤掉里面的空值,返回列表
     * 注意：为了可以做add操作,采用了初始化arrayList的结果
     * eg:
     * 1,2,3,,,,,,9,,,, => ["1","2","3","9"]
     *
     * @param str 1,2,3,4,5
     * @return ["1","2","3"]
     */
    public static List<String> split(String str) {
        //根据,拆分
        return split(str, ",");
    }

    /**
     * 根据逗号拆分str并过滤掉里面的空值,返回列表
     * 注意：为了可以做add操作,采用了初始化arrayList的结果
     * eg:
     * 1,2,3,,,,,,9,,,, => ["1","2","3","9"]
     *
     * @param str 1,2,3,4,5
     * @param sym 符号
     * @return ["1","2","3"]
     */
    public static List<String> split(String str, String sym) {
        //初始化列表
        List<String> result = new ArrayList<>();
        //判空
        if (StringUtils.isBlank(str)) {
            //返回
            return result;
        }
        //根据符号拆分
        String[] arr = str.split(sym);
        //循环
        for (String s : arr) {
            //判空
            if (StringUtils.isBlank(s)) {
                //过
                continue;
            }
            //组装
            result.add(s);
        }
        //默认
        return result;
    }

    /**
     * 将list 转化为 string
     * eg: ["test", "test2"] - > test,test2
     *
     * @param list
     * @return
     */
    public static String toString(List<String> list) {
        //初始化
        StringBuffer str = new StringBuffer();
        //判空
        if (CollectionUtils.isEmpty(list)) {
            //返回
            return str.toString();
        }
        //循环
        for (String s : list) {
            //判空
            if (StringUtils.isBlank(s)) {
                //过
                continue;
            }
            //组装
            str.append(s + ",");
        }
        //如果有长度
        if (str.length() > 0) {
            //删除最后一个,
            str = str.deleteCharAt(str.length() - 1);
        }
        //返回结果
        return str.toString();
    }

    /**
     * 从列表中随机拿一个对象,概率完全相等
     *
     * @param list 列表
     * @return
     */
    public static <T> T randomOne(List<T> list) {
        //判空
        if (CollectionUtils.isEmpty(list)) {
            //过
            return null;
        }
        //随机数取一个
        return list.get(new Random().nextInt(list.size()));
    }

}
