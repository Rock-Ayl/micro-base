package com.rock.micro.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * 获取json指定路径下的对象
     *
     * @param json 实体对象  深度嵌套对象
     * @param path 路径     用.切割 eg: [path=product.skuList.sku]
     * @return
     */
    public static Object getPathObject(JSONObject json, String path) {
        //实现,返回第一个
        return getPathList(json, path)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取json指定路径下的对象
     *
     * @param json         实体对象  深度嵌套对象
     * @param path         路径     用.切割 eg: [path=product.skuList.sku]
     * @param toJavaObject 指定强转的对象
     * @return
     */
    public static <T> T getPathObject(JSONObject json, String path, Class<T> toJavaObject) {
        //实现,返回第一个
        return getPathList(json, path, toJavaObject)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取json指定路径下的对象列表
     *
     * @param json         实体对象  深度嵌套对象
     * @param path         路径     用.切割 eg: [path=product.skuList.sku]
     * @param toJavaObject 指定强转的对象
     * @return
     */
    public static <T> List<T> getPathList(JSONObject json, String path, Class<T> toJavaObject) {
        //收集结果
        List<Object> collectList = getPathList(json, path);
        //初始化结果列表
        List<T> resultList = new ArrayList<>();
        //循环
        for (Object object : collectList) {
            //强转
            resultList.add(toJavaObject.cast(object));
        }
        //返回
        return resultList;
    }

    /**
     * 获取json指定路径下的对象列表
     *
     * @param json 实体对象  深度嵌套对象
     * @param path 路径     用.切割 eg: [path=product.skuList.sku]
     * @return
     */
    public static List<Object> getPathList(JSONObject json, String path) {
        //判空
        if (json == null || path == null) {
            //过
            return new ArrayList<>();
        }
        //拆分路径
        String[] pathArr = path.split("\\.");
        //初始化结果列表
        List<Object> collectList = new ArrayList<>();
        //递归、不断拆解并组装至结果
        getPathArr(collectList, json, pathArr, 0);
        //返回
        return collectList;
    }

    /**
     * 递归,获取json指定路径下的对象列表实现
     *
     * @param resultList 结果集
     * @param json       递归的深度嵌套对象
     * @param pathArr    路径数组
     * @param index      路径数组的索引
     */
    private static void getPathArr(List<Object> resultList, JSONObject json, String[] pathArr, int index) {
        //判空
        if (json == null || index >= pathArr.length) {
            //过
            return;
        }
        //获取当前key
        String key = pathArr[index];
        //判空
        if (key == null) {
            //过
            return;
        }
        //获取对应对象
        Object object = json.get(key);
        //判空
        if (object == null) {
            //过
            return;
        }
        //如果是目标对象
        if (index == pathArr.length - 1) {
            //组装结果
            resultList.add(object);
            //过
            return;
        }
        //如果是json对象
        if (object instanceof JSONObject) {
            //递归
            getPathArr(resultList, (JSONObject) object, pathArr, index + 1);
            //过
            return;
        }
        //如果是集合
        if (object instanceof Collection) {
            //转为集合,循环
            for (Object next : (Collection) object) {
                //如果是json对象
                if (next instanceof JSONObject) {
                    //递归
                    getPathArr(resultList, (JSONObject) next, pathArr, index + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        String str = "{\"ProductId\":14551,\"ProductStyle\":\"POD55l9o\",\"propertyList\":[{\"detailList\":[{\"isSelect\":true,\"IsMissing\":true,\"InputList\":null,\"PropertyDetailId\":0,\"DetailName\":33222,\"PropertyId\":1,\"RelatedPropertyId\":0,\"InputNames\":\"\"}],\"SpecCode\":\"Spec3\",\"PropertyId\":1,\"PropertyType\":1,\"PropertyName\":\"jfwoehj\",\"Desc\":\"13121\",\"ImageLink\":\"/UploadFiles/79/ProductProperty/自动化1(2).png\",\"VideoLink\":\"/UploadFiles/79/ProductProperty/oceans(7).mp4\",\"ValidStatus\":1,\"CreateTime\":\"2025-04-09T16:41:33.43\",\"CreatorId\":79,\"UpdateTime\":\"2025-04-10T17:03:10.577\",\"UpdaterId\":79}],\"variationList\":[{\"erpProductVariationValues\":[{\"ProductVariationValueId\":132880,\"ProductVariationId\":20847,\"VariationValue\":\"Beige\",\"ListOrder\":1,\"ProductId\":14551,\"VariationTitle\":\"颜色\",\"VariationItem\":\"Spec1\"}],\"ProductVariationId\":20847,\"ProductId\":14551,\"VariationTitle\":\"颜色\",\"ListOrder\":1,\"VariationItem\":\"Spec1\"},{\"erpProductVariationValues\":[{\"ProductVariationValueId\":132882,\"ProductVariationId\":20848,\"VariationValue\":\"XS\",\"ListOrder\":1,\"ProductId\":14551,\"VariationTitle\":\"尺寸\",\"VariationItem\":\"Spec2\"}],\"ProductVariationId\":20848,\"ProductId\":14551,\"VariationTitle\":\"尺寸\",\"ListOrder\":2,\"VariationItem\":\"Spec2\"}]}";
        //转为json
        JSONObject json = deepClone(str, JSONObject.class);
        //尝试收集
        List<Object> path = getPathList(json, "variationList.erpProductVariationValues.VariationTitle");
        List<String> path2 = getPathList(json, "variationList.erpProductVariationValues.VariationTitle", String.class);
        Object path3 = getPathObject(json, "variationList.erpProductVariationValues.VariationTitle");
        String path4 = getPathObject(json, "variationList.erpProductVariationValues.VariationTitle", String.class);
        System.out.println();
    }

}