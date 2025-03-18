package com.rock.micro.base.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数组 扩展扩展包
 *
 * @Author ayl
 * @Date 2023-03-03
 */
public class ArrayExtraUtils {

    /**
     * 将 string 转为 String[]
     *
     * @param fields eg: id,productSku,developerList
     * @return
     */
    public static String[] toArray(String fields) {
        //判空
        if (fields == null) {
            //过
            return new String[]{};
        }
        //实现
        return ListExtraUtils.split(fields).toArray(new String[]{});
    }

    /**
     * 将 int[] 分组
     *
     * @param nums
     * @return
     */
    public Map<Integer, List<Integer>> groupList(int[] nums) {
        //判空
        if (nums == null) {
            //过
            return new HashMap<>();
        }
        //返回
        return Arrays.stream(nums)
                //装箱
                .boxed()
                //分组统计数量
                .collect(Collectors.groupingBy(p -> p));
    }

    /**
     * 将 int[] 分组并统计数量
     *
     * @param nums
     * @return
     */
    public Map<Integer, Long> groupCount(int[] nums) {
        //判空
        if (nums == null) {
            //过
            return new HashMap<>();
        }
        //返回
        return Arrays.stream(nums)
                //装箱
                .boxed()
                //分组统计数量
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

}
