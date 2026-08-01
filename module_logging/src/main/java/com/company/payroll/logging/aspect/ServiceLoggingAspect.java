package com.company.payroll.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceLayerPointcut() {
        // Pointcut signature
    }

    @Around("serviceLayerPointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String methodName = joinPoint.getSignature().getName();

        logger.info("{} start.", methodName);
        long startTime = System.currentTimeMillis();

        // TODO - to enhance the catch exception on business exception to differentiate between business exception and system exception, and log accordingly
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("{} end. Execution time: {} ms", methodName, executionTime);
            return result;
        } catch (Throwable throwable) {
            logger.error("{} failed with exception: {}", methodName, throwable.getMessage());
            throw throwable;
        }
    }
}
