package com.rock.micro.base.data.doc;

import com.rock.micro.base.data.BaseDocument;
import com.rock.micro.base.data.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口日志
 *
 * @Author ayl
 * @Date 2025-08-08
 */
//链式构造、无参构造、全参构造
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ApiModel("接口 日志实体")
@Document(collection = "micro_base_api_log")
public class ApiLogDoc extends BaseDocument {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务名")
    private String serviceName;

    @ApiModelProperty("方法签名,例如controller.method")
    private String method;

    @ApiModelProperty("请求url")
    private String url;

    @ApiModelProperty("http请求方式[GET][POST]")
    private String httpMethod;

    @ApiModelProperty("请求参数json字符串")
    private String params;

    @ApiModelProperty("客户端ip地址")
    private String ip;

    @ApiModelProperty("请求开始时间")
    private Date startTime;

    @ApiModelProperty("请求结束时间")
    private Date endTime;

    @ApiModelProperty("请求耗时,单位毫秒")
    private BigDecimal duration;

    /**
     * 用户信息
     */

    @ApiModelProperty("创建用户")
    private User createUser;

}
