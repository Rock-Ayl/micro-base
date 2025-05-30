package com.rock.micro.base.data.doc;

import com.alibaba.fastjson.JSONObject;
import com.rock.micro.base.data.BaseDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 通用日志
 *
 * @Author ayl
 * @Date 2025-05-30
 */
@Setter
@Getter
@ApiModel("普通 日志实体")
@Document(collection = "micro_base_normal_log")
public class NormalLogDoc extends BaseDocument {

    private static final long serialVersionUID = 1L;

    /**
     * 一般信息
     */

    @ApiModelProperty("日志类型")
    private String type;

    @ApiModelProperty("日志备注")
    private String remark;

    @ApiModelProperty("日志额外记录Json")
    private JSONObject extraJson;

    /**
     * 时间信息
     */

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("结束时间 - 开始时间")
    private Long rangeTime;

    /**
     * 用户信息
     */

    @ApiModelProperty("创建用户id")
    private String createUserId;

    @ApiModelProperty("创建用户名称")
    private String createUserName;

    @ApiModelProperty("创建用户邮箱")
    private String createUserEmail;

}
