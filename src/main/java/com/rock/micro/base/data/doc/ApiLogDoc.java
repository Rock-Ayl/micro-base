package com.rock.micro.base.data.doc;

import com.rock.micro.base.data.BaseDocument;
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

    /**
     * {@link com.rock.micro.base.enums.ApiLogHttpMethodEnum } 枚举
     */
    @ApiModelProperty("http请求方式 枚举")
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

    @ApiModelProperty("创建用户id")
    private String createUserId;

    @ApiModelProperty("创建用户名称")
    private String createUserName;

    @ApiModelProperty("创建用户邮箱")
    private String createUserEmail;

    /**
     * 地理信息
     */

    @ApiModelProperty("国家-编码")
    private String countryShort;

    @ApiModelProperty("国家-名称")
    private String countryLong;

    @ApiModelProperty("州、省")
    private String region;

    @ApiModelProperty("城市")
    private String city;

}
