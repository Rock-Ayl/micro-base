package com.rock.micro.base.serivce.impl;

import com.rock.micro.base.common.auth.LoginAuth;
import com.rock.micro.base.common.auth.ServiceInfo;
import com.rock.micro.base.data.doc.ApiLogDoc;
import com.rock.micro.base.db.mongo.BaseMongoServiceImpl;
import com.rock.micro.base.enums.ApiLogHttpMethodEnum;
import com.rock.micro.base.serivce.ApiLogDocService;
import com.rock.micro.base.util.FastJsonExtraUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Service
public class ApiLogDocServiceImpl extends BaseMongoServiceImpl<ApiLogDoc> implements ApiLogDocService {

    //允许的参数最大字节数 16KB
    private static final int PARAMS_MAX_BYTES = 16 * 1024;

    @Override
    public ApiLogDoc parse(HttpServletRequest request, ProceedingJoinPoint joinPoint, String method, long start, long end) {

        //构建日志实体
        ApiLogDoc apiLogDoc = new ApiLogDoc();

        //服务
        apiLogDoc.setServiceName(ServiceInfo.STATIC_SERVICE_NAME);
        //接口方法名
        apiLogDoc.setMethod(method);
        //接口地址
        apiLogDoc.setUrl(request.getRequestURI());

        //解析http请求类型
        ApiLogHttpMethodEnum apiLogHttpMethodEnum = ApiLogHttpMethodEnum.parseByCode(request.getMethod());
        //http请求类型
        apiLogDoc.setHttpMethod(apiLogHttpMethodEnum.getCode());

        //获取参数
        Object[] args = joinPoint.getArgs();
        //如果有参数
        if (args != null && args.length > 0) {
            try {
                //解析为参数数组Str
                String paramArrStr = FastJsonExtraUtils.toJSONString(args);
                //如果参数没有超过最大字节数
                if (paramArrStr != null && paramArrStr.getBytes(StandardCharsets.UTF_8).length <= PARAMS_MAX_BYTES) {
                    //设置到参数里(允许存入库)
                    apiLogDoc.setParams(paramArrStr);
                }
            } catch (Exception e) {
                //如果异常(一般是解析文件),则用通用的解析,这种本质是toString(),不会报错,还能把普通参数和实体名解析出来
                apiLogDoc.setParams(Arrays.toString(args));
            }
        }

        //获取请求用户的ip而非是网关的ip   .ip(request.getRemoteAddr())
        apiLogDoc.setIp(getClientIp(request));

        //时间
        apiLogDoc.setStartTime(new Date(start));
        apiLogDoc.setEndTime(new Date(end));
        apiLogDoc.setDuration(new BigDecimal(end - start));

        /**
         * 用户
         */

        //如果线程里有用户信息
        if (LoginAuth.USER.get() != null) {
            //记录
            apiLogDoc.setCreateUserId(LoginAuth.USER.get().getId());
            apiLogDoc.setCreateUserName(LoginAuth.USER.get().getName());
            apiLogDoc.setCreateUserEmail(LoginAuth.USER.get().getEmail());
        }

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