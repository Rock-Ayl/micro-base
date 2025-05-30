package com.rock.micro.base.common.aop;

import com.rock.micro.base.serivce.NormalLogDocService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务 aop
 *
 * @Author ayl
 * @Date 2025-03-26
 */
@Aspect
@Component
public class ScheduledAOPAspect {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledAOPAspect.class);

    @Autowired
    private NormalLogDocService normalLogDocService;

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object logScheduledTask(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取任务名称
        String taskName = joinPoint.getSignature().toShortString();
        //返回对象
        Object result = null;
        //获取开始时间
        long startTime = System.currentTimeMillis();
        try {
            //执行并返回
            result = joinPoint.proceed();
            //结束时间
            long endTime = System.currentTimeMillis();
            //输出
            logger.info("Scheduled aop [{}] success，cost {} ms", taskName, endTime - startTime);
        } catch (Exception e) {
            logger.error("Scheduled aop [{}] fail，error: {}", taskName, e.getMessage(), e);
            //记录日志
            normalLogDocService.createLog("scheduled", taskName, startTime, System.currentTimeMillis(), e);
            throw e;
        }
        //返回
        return result;
    }

}