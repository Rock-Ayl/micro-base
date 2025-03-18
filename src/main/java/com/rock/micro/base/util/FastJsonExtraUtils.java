package com.rock.micro.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FastJson 扩展工具包
 *
 * @Author ayl
 * @Date 2022-08-25
 */
public class FastJsonExtraUtils {

    /**
     * 深克隆单个对象,也可以将一个对象转化为另一个对象(当然,结构得基本一致或继承关系)
     *
     * @param object       源对象,比如父对象,不能是数组等结构
     * @param toJavaObject 目标对象
     * @return
     */
    public static <T> T deepClone(Object object, Class<T> toJavaObject) {
        //判空
        if (object == null) {
            //过
            return null;
        }
        //先转为string再转为对应实体,如果转为json对象再转实体某些特殊情况会报错
        return JSON.parseObject(toJSONString(object), toJavaObject);
    }

    /**
     * 深克隆为数组对象,也可以将一个对象数组转化为另一个对象数组(当然,结构得基本一致或继承关系)
     *
     * @param listOrArrObject 源对象,比如父对象,必须是数组、集合等结构
     * @param toJavaObject    目标对象
     * @return
     */
    public static <T> List<T> deepCloneList(Object listOrArrObject, Class<T> toJavaObject) {
        //判空
        if (listOrArrObject == null) {
            //过
            return new ArrayList<>();
        }
        //先转为string再转为对应实体,如果转为json对象再转实体某些特殊情况会报错
        return JSON.parseArray(toJSONString(listOrArrObject), toJavaObject);
    }

    /**
     * 对象转String {@link Object} -> {@link String}
     *
     * @param object 对象
     * @return
     */
    public static String toJSONString(Object object) {
        //判空
        if (object == null) {
            //过
            return null;
        }
        //如果就是string
        if (object instanceof String) {
            //直接返回,无需再转
            return object.toString();
        }
        //实现
        return JSON.toJSONString(object);
    }

    /**
     * 给json某个key该名称,注意,如果新名称存在,会被覆盖
     *
     * @param json    json
     * @param oldName 旧名称
     * @param newName 新名称
     */
    public static void rename(JSONObject json, String oldName, String newName) {
        //判空
        if (json == null || oldName == null || newName == null) {
            //过
            return;
        }
        //如果不存在旧名称
        if (json.containsKey(oldName) == false) {
            //过
            return;
        }
        //获取值
        Object value = json.get(oldName);
        //删除旧key
        json.remove(oldName);
        //覆盖新名称
        json.put(newName, value);
    }

    /**
     * 展开json的某个key(key必须为列表) (类似mongo的unwind操作,但保底留一条数据)
     *
     * @param json      被展开的对象
     * @param unwindKey 被展开的数组
     * @return
     */
    public static List<JSONObject> unwind(JSONObject json, String unwindKey) {
        //判空
        if (json == null || unwindKey == null) {
            //默认
            return new ArrayList<>();
        }
        //如果不存在
        if (json.containsKey(unwindKey) == false) {
            //保底自己深克隆一个
            return new ArrayList<>(Collections.singletonList(deepClone(json, JSONObject.class)));
        }
        //如果不是列表
        if (json.get(unwindKey) instanceof List == false) {
            //保底自己深克隆一个
            return new ArrayList<>(Collections.singletonList(deepClone(json, JSONObject.class)));
        }
        //获取unwind的数据列表
        JSONArray jsonArray = json.getJSONArray(unwindKey);
        //判空
        if (CollectionUtils.isEmpty(jsonArray)) {
            //克隆出来一个新的
            JSONObject cloneJson = deepClone(json, JSONObject.class);
            //删除对应key
            cloneJson.remove(unwindKey);
            //转为列表结构
            return new ArrayList<>(Collections.singletonList(cloneJson));
        }
        //初始化结果
        List<JSONObject> result = new ArrayList<>();
        //循环
        for (Object o : jsonArray) {
            //先深克隆一个
            JSONObject cloneJson = deepClone(json, JSONObject.class);
            //判空
            if (o != null) {
                //覆盖对应key
                cloneJson.put(unwindKey, o);
            } else {
                //删除对应key
                cloneJson.remove(unwindKey);
            }
            //组装
            result.add(cloneJson);
        }
        //返回
        return result;
    }

}