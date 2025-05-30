package com.rock.micro.base.common.api;

import com.alibaba.fastjson.JSONObject;
import com.rock.micro.base.common.auth.ServiceInfo;
import com.rock.micro.base.common.constant.JSONConst;
import com.rock.micro.base.util.JacksonExtraUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 响应内容封装器,方便控制层组装返回.
 *
 * @Author ayl
 * @Date 2022-03-09
 */
public class JSONResponse {

    //该class本质基本上是一个Json
    private Map<String, Object> response;

    //私有化
    private JSONResponse() {
        //同时生成对应json
        this.response = new HashMap<>();
    }

    /**
     * 成功
     *
     * @return
     */
    public static JSONResponse success() {
        //初始化
        JSONResponse response = new JSONResponse();
        //默认
        response.response.put(JSONConst.KEY_STATE, JSONConst.VALUE_SUCCESS);
        response.response.put(JSONConst.KEY_CODE, 0);
        response.response.put(JSONConst.KEY_PORT, ServiceInfo.STATIC_PORT);
        //返回
        return response;
    }

    /**
     * 失败
     *
     * @return
     */
    public static JSONResponse error() {
        //初始化
        JSONResponse response = new JSONResponse();
        //组装error
        response.response.put(JSONConst.KEY_STATE, JSONConst.VALUE_ERROR);
        response.response.put(JSONConst.KEY_CODE, 1);
        response.response.put(JSONConst.KEY_PORT, ServiceInfo.STATIC_PORT);
        response.response.put(JSONConst.KEY_ERROR_MSG, "接口请求异常");
        //返回
        return response;
    }

    /**
     * 失败+msg
     *
     * @param e
     * @return
     */
    public static JSONResponse error(Throwable e) {
        //初始化z
        JSONResponse response = new JSONResponse();
        //组装error
        response.response.put(JSONConst.KEY_STATE, JSONConst.VALUE_ERROR);
        response.response.put(JSONConst.KEY_CODE, 1);
        response.response.put(JSONConst.KEY_PORT, ServiceInfo.STATIC_PORT);
        response.response.put(JSONConst.KEY_ERROR_MSG, Optional.ofNullable(e)
                .map(Throwable::getMessage)
                .orElse("接口请求异常")
        );
        //返回
        return response;
    }

    /**
     * 失败+msg
     *
     * @param e
     * @return
     */
    public static JSONResponse error(MyException e) {
        //初始化
        JSONResponse response = new JSONResponse();
        //组装error
        response.response.put(JSONConst.KEY_STATE, JSONConst.VALUE_ERROR);
        response.response.put(JSONConst.KEY_CODE, 1);
        response.response.put(JSONConst.KEY_PORT, ServiceInfo.STATIC_PORT);
        //初始化错误体
        JSONObject errorBody;
        //如果是 开发人员场景抛出
        if (e != null && e.getZhDesc() != null) {
            //初始化
            errorBody = new JSONObject();
            //组装
            errorBody.put("code", MyExceptionEnum.NORMAL_ERROR.getCode());
            errorBody.put("zhDesc", e.getZhDesc());
        }
        //默认用 对用户通用国际化枚举 处理
        else {
            //枚举转json
            errorBody = Optional.ofNullable(e)
                    //获取异常枚举
                    .map(MyException::getMyExceptionEnum)
                    //默认
                    .orElse(MyExceptionEnum.NORMAL_ERROR)
                    //转为json
                    .toJSON();
            //获取错误的key
            errorBody.put("errorKey", Optional.ofNullable(e)
                    //获取错误key
                    .map(MyException::getErrorKey)
                    //默认
                    .orElse(null));
        }
        //组装错误体
        response.response.put(JSONConst.KEY_ERROR_BODY, errorBody);
        //返回
        return response;
    }

    /**
     * 组装返回结果
     *
     * @param value
     * @return
     */
    public JSONResponse putResult(Object value) {
        //固定key
        this.response.put(JSONConst.KEY_RESULT, value);
        //返回
        return this;
    }

    /**
     * 组装一般key
     *
     * @param key
     * @param value
     * @return
     */
    public JSONResponse put(String key, Object value) {
        this.response.put(key, value);
        return this;
    }

    /**
     * 重写toString
     *
     * @return
     */
    @Override
    public String toString() {
        //实现
        return JacksonExtraUtils.toJSONString(this.response);
    }

}
