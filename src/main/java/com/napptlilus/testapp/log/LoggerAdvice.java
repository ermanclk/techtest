package com.napptlilus.testapp.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class LoggerAdvice {

    private Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);

    /**
     * For methods annotated @LogMe("message"), adds new log line "message", before executing method
     * @param joinPoint
     */
    @Before("@annotation(com.napptlilus.testapp.log.LogMe)")
    public void logAnnotatedMethodAndMessage(JoinPoint joinPoint) {

        LogMe annotation = getTargetMethod(joinPoint).getAnnotation(LogMe.class);
        if (annotation != null) {
            logger.info("{}, {}",getMethodInfo(joinPoint), annotation.value());
        }
    }

    /**
     * For methods annotated @LogMe("message"), adds new log line "message", after method executed
     * @param joinPoint
     * @param result
     * @throws Throwable
     */
    @AfterReturning(pointcut = "@annotation(com.napptlilus.testapp.log.LogMe)", returning = "result")
    public void logAfterAnnotatedMethodCall(JoinPoint joinPoint, Object result) {
        logger.info("Executed {} returned: {}", getMethodInfo(joinPoint), getReturnValue(result));
    }

    /**
     * Defines pointcut
     */
    @Pointcut("execution( public * com.napptlilus.testapp..*(..))")
    public void logMethods() {
    }

    /**
     * Logs before method execution on defined pointcut
     * @param joinPoint
     */
    @Before("logMethods()")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        logger.debug("Executing {}, input args: {}", getMethodInfo(joinPoint), argsToString(joinPoint.getArgs()));
    }

    /**
     * Logs before method execution on defined pointcut
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "logMethods()", returning = "result")
    public void logAfterMethodCall(JoinPoint joinPoint, Object result) {
        logger.debug("Executed {}, returned: {} ", getMethodInfo(joinPoint), getReturnValue(result));
    }

    /**
     * Logs on thrown exceptions
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "logMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        logger.error("Failed execution {}, details: {}", getMethodInfo(joinPoint), ex.getMessage());
    }

    private Method getTargetMethod(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    private String getReturnValue(Object result) {
        return (result != null) ? result.toString() : "null or void method";
    }

    private String getMethodInfo(JoinPoint joinPoint) {
        Method method = getTargetMethod(joinPoint);
        return "[" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "]";
    }

    private String argsToString(Object[] objList) {
        StringBuilder params = new StringBuilder();
        if (objList != null) {
            for (Object arg : objList) {
                params.append(" " + arg);
            }
        }
        return params.toString();
    }

}