package com.rock.micro.base.controller;

import com.rock.micro.base.common.api.JSONResponse;
import com.rock.micro.base.common.constant.HttpConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统统一接口
 */
@Api(tags = "系统模块")
@RestController
@RequestMapping(value = "/system")
public class SystemApiController {

    @ApiOperation(value = "判断系统是否健康(用于检测服务启动成功后心跳)")
    @GetMapping(value = "/health", produces = HttpConst.RESPONSE_HEADERS_CONTENT_TYPE_APPLICATION_JSON)
    public String health() {
        //返回
        return JSONResponse.success().toString();
    }

}