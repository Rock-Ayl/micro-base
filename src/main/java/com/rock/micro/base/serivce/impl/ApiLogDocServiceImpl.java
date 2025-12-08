package com.rock.micro.base.serivce.impl;

import com.rock.micro.base.common.auth.LoginAuth;
import com.rock.micro.base.common.auth.ServiceInfo;
import com.rock.micro.base.data.doc.ApiLogDoc;
import com.rock.micro.base.db.mongo.BaseMongoServiceImpl;
import com.rock.micro.base.serivce.ApiLogDocService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Service
public class ApiLogDocServiceImpl extends BaseMongoServiceImpl<ApiLogDoc> implements ApiLogDocService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiLogDoc parse(HttpServletRequest request, ProceedingJoinPoint joinPoint, String method, long start, long end) {

        //构建日志实体
        ApiLogDoc apiLogDoc = ApiLogDoc.builder()
                .serviceName(ServiceInfo.STATIC_SERVICE_NAME)
                .method(method)
                .url(request.getRequestURI())
                .httpMethod(request.getMethod())
                .params(Arrays.toString(joinPoint.getArgs()))

                //获取请求用户的ip而非是网关的ip   .ip(request.getRemoteAddr())
                .ip(getClientIp(request))

                .startTime(new Date(start))
                .endTime(new Date(end))
                .duration(new BigDecimal(end - start))
                .build();

        /**
         * 用户
         */

        //线程用户信息(不一定有)
        apiLogDoc.setCreateUser(LoginAuth.USER.get());

        //返回
        return apiLogDoc;
    }

    /**
     * 获取 网关给的 请求ip
     *
     * @param request
     * @return
     */
    private static String getClientIp(HttpServletRequest request) {
        //获取 nginx 携带的 用户ip
        String ip = request.getHeader("X-Forwarded-For");
        //判空
        if (StringUtils.isNotBlank(ip) && "unknown".equalsIgnoreCase(ip) == false) {
            //多级代理时，第一个ip为客户端真实ip
            if (ip.contains(",")) {
                //返回
                return ip.split(",")[0].trim();
            } else {
                //默认
                return ip;
            }
        }
        //获取 nginx 携带的 用户ip 第二顺位
        ip = request.getHeader("X-Real-IP");
        //判空
        if (StringUtils.isNotBlank(ip) && "unknown".equalsIgnoreCase(ip) == false) {
            //返回
            return ip;
        }
        //默认用直连的,如果是nginx请求,应该在上面就被拦了下来,而普通直连请求会走这里,比如健康检查
        return request.getRemoteAddr();
    }

}