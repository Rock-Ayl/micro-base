package com.rock.micro.base.common.api;

import com.rock.micro.base.common.enums.HttpStatusEnum;
import com.rock.micro.base.util.FastJsonExtraUtils;

import java.util.HashMap;
import java.util.Map;

import static com.rock.micro.base.common.constant.JSONConst.*;

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
        //组装success
        response.response.put(KEY_CODE, VALUE_SUCCESS);
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
        response.response.put(KEY_CODE, HttpStatusEnum.INTERNAL_SERVER_ERROR);
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
        //初始化
        JSONResponse response = new JSONResponse();
        //组装error
        response.response.put(KEY_CODE, HttpStatusEnum.INTERNAL_SERVER_ERROR);
        //判空
        if (e != null) {
            //转为json并组装
            response.response.put(KEY_ERROR_MSG, e.getMessage());
        }
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
        this.response.put(KEY_RESULT, value);
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
        return FastJsonExtraUtils.toJSONString(this.response);
    }

}
