package com.rock.micro.base.common.aop;

import com.rock.micro.base.data.doc.ApiLogDoc;
import com.rock.micro.base.serivce.ApiLogDocService;
import com.rock.micro.base.serivce.BatchLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制层 aop
 * 默认执行顺序:
 * 1. 环绕通知-调用前 {@link org.aspectj.lang.annotation.Around}
 * 2. 前置通知 {@link org.aspectj.lang.annotation.Before}
 * 3. 返回后通知 {@link org.aspectj.lang.annotation.AfterReturning}
 * 4. 后置通知 {@link org.aspectj.lang.annotation.After}
 * 5. 环绕通知-调用后 {@link org.aspectj.lang.annotation.Around}
 *
 * @Author ayl
 * @Date 2024-12-29
 */
@Component
//切面注解,指定改类为切面类
@Aspect
public class ControllerAOPAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerAOPAspect.class);

    @Autowired
    private BatchLogService batchLogService;

    @Autowired
    private ApiLogDocService apiLogDocService;

    /**
     * 控制层指针
     * 目前是 基于 方法执行 的切点
     * 还有一种: @Pointcut(value = "@annotation(org.springframework.scheduling.annotation.Scheduled)")
     * 这种是 基于注解 的切点，用于检测注解修饰的方法
     */
    @Pointcut("execution(* com.rock.micro.*.controller.*.*(..))")
    public void controllerPointcut() {
    }

    //前置通知
    @Before("controllerPointcut()")
    public void doBefore() {
        LOG.info("ControllerAspect do before...");
    }

    //后置通知
    @After("controllerPointcut()")
    public void doAfter() {
        LOG.info("ControllerAspect do after...");
    }

    //环绕通知
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //开始时间
        long start = System.currentTimeMillis();
        //获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //接口返回结果
        Object result;
        try {
            //执行接口本身
            result = joinPoint.proceed();
        } finally {
            //结束时间
            long end = System.currentTimeMillis();
            //构建日志实体
            ApiLogDoc logEntity = apiLogDocService.parse(
                    request, joinPoint,
                    joinPoint.getSignature().toShortString(),
                    start, end);
            //加入队列
            batchLogService.addLog(logEntity);
        }
        //返回结果
        return result;
    }

    //返回后通知
    @AfterReturning("controllerPointcut()")
    public void doAfterReturning() {
        LOG.info("ControllerAspect do AfterReturning...");
    }

    //抛出异常后通知
    @AfterThrowing("controllerPointcut()")
    public void doAfterThrowing() {
        LOG.info("ControllerAspect do AfterThrowing...");
    }

}
