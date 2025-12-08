package com.rock.micro.base.serivce;

import com.rock.micro.base.data.doc.ApiLogDoc;
import com.rock.micro.base.db.mongo.BaseMongoService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApiLogDocService extends BaseMongoService<ApiLogDoc> {

    /**
     * 初始化实体
     *
     * @param request   请求
     * @param joinPoint 切面方法
     * @param method    方法
     * @param start     开始时间
     * @param end       结束时间
     * @return
     */
    ApiLogDoc parse(HttpServletRequest request, ProceedingJoinPoint joinPoint, String method, long start, long end);

    @Getter
    @Setter
    @ApiModel("IP统计结果")
    public class IpStatisticResult {

        @ApiModelProperty("统计数量")
        private Integer count;

        @ApiModelProperty("id列表")
        private List<String> idList;

        @ApiModelProperty("ip地址")
        private String ip;
    }

}