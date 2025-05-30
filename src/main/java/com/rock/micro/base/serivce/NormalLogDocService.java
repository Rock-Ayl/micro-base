package com.rock.micro.base.serivce;

import com.alibaba.fastjson.JSONObject;
import com.rock.micro.base.data.doc.NormalLogDoc;
import com.rock.micro.base.db.mongo.BaseMongoService;

public interface NormalLogDocService extends BaseMongoService<NormalLogDoc> {

    /**
     * 创建 通用 日志
     *
     * @param type      类型
     * @param remark    备注
     * @param extraJson 额外需要记录的json
     */
    void createLog(String type, String remark, JSONObject extraJson);

    /**
     * 创建 异常 日志
     *
     * @param type      类型
     * @param remark    备注
     * @param exception 额外需要记录的异常
     */
    void createLog(String type, String remark, Exception exception);

    /**
     * 创建 时间 日志
     *
     * @param type
     * @param remark
     * @param startTime
     * @param endTime
     * @param extraJson
     */
    void createLog(String type, String remark, Long startTime, Long endTime, JSONObject extraJson);

    /**
     * 创建 时间 + 异常 日志
     *
     * @param type
     * @param remark
     * @param startTime
     * @param endTime
     * @param exception
     */
    void createLog(String type, String remark, Long startTime, Long endTime, Exception exception);

}