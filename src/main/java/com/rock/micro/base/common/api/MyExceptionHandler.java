package com.rock.micro.base.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制层全局异常捕获
 *
 * @Author ayl
 * @Date 2023-03-29
 */
@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public Object exceptionHandler(MyException e) {
        LOG.error("MyExceptionHandler catch MyException error:", e);
        //返回统一异常返回
        return JSONResponse.error(e).toString();
    }

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Object exceptionHandler(Throwable e) {
        LOG.error("MyExceptionHandler catch Throwable error:", e);
        //返回统一异常返回
        return JSONResponse.error(e).toString();
    }

}
