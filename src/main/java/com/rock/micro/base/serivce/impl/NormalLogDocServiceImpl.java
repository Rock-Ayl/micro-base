package com.rock.micro.base.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rock.micro.base.common.auth.LoginAuth;
import com.rock.micro.base.data.doc.NormalLogDoc;
import com.rock.micro.base.db.mongo.BaseMongoServiceImpl;
import com.rock.micro.base.serivce.NormalLogDocService;
import org.springframework.stereotype.Service;

@Service
public class NormalLogDocServiceImpl extends BaseMongoServiceImpl<NormalLogDoc> implements NormalLogDocService {

    @Override
    public void createLog(String type, String remark, JSONObject extraJson) {
        //初始化
        NormalLogDoc create = init(type, remark, null, null);
        //组装json
        create.setExtraJson(extraJson);
        //插入日志
        this.create(create);
    }

    @Override
    public void createLog(String type, String remark, Exception exception) {
        //初始化
        NormalLogDoc create = init(type, remark, null, null);
        //构建异常日志
        buildExtraJson(create, exception);
        //插入日志
        this.create(create);
    }

    @Override
    public void createLog(String type, String remark, Long startTime, Long endTime, JSONObject extraJson) {
        //初始化
        NormalLogDoc create = init(type, remark, startTime, endTime);
        //组装json
        create.setExtraJson(extraJson);
        //插入日志
        this.create(create);
    }

    @Override
    public void createLog(String type, String remark, Long startTime, Long endTime, Exception exception) {
        //初始化
        NormalLogDoc create = init(type, remark, startTime, endTime);
        //构建异常日志
        buildExtraJson(create, exception);
        //插入日志
        this.create(create);
    }

    /**
     * 构建异常字段
     *
     * @param doc       实体
     * @param exception 异常对象
     */
    private void buildExtraJson(NormalLogDoc doc, Exception exception) {
        try {
            //异常转为string,有可能有异常
            doc.setExtraJson(JSON.parseObject(JSON.toJSONString(exception)));
        } catch (Exception e1) {
            //记录特殊情况日志
            JSONObject errorJson = new JSONObject();
            errorJson.put("特殊情况", "Exception转换失败");
            errorJson.put("error1String", e1.toString());
            errorJson.put("error1Message", e1.getMessage());
            errorJson.put("errorString", exception.toString());
            errorJson.put("errorMessage", exception.getMessage());
            doc.setExtraJson(errorJson);
        }
    }

    /**
     * 统一初始化
     *
     * @return
     */
    private NormalLogDoc init(String type, String remark, Long startTime, Long endTime) {
        //初始化
        NormalLogDoc normalLogDoc = new NormalLogDoc();

        /**
         * 一般
         */

        //组装参数
        normalLogDoc.setType(type);
        normalLogDoc.setRemark(remark);

        /**
         * 时间
         */

        normalLogDoc.setStartTime(startTime);
        normalLogDoc.setEndTime(endTime);
        //如果有开始、结束时间
        if (startTime != null && endTime != null) {
            //计算区间并组装
            normalLogDoc.setRangeTime(endTime - startTime);
        }

        /**
         * 用户
         */

        //如果线程里有用户信息
        if (LoginAuth.USER.get() != null) {
            //记录
            normalLogDoc.setCreateUserId(LoginAuth.USER.get().getId());
            normalLogDoc.setCreateUserName(LoginAuth.USER.get().getName());
            normalLogDoc.setCreateUserEmail(LoginAuth.USER.get().getEmail());
        }
        //返回
        return normalLogDoc;
    }

}