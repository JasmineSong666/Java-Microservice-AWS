package com.example.JobService.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

//        // return-type  classname.methodname(args)  // * and .. represent all
//        @Before("execution(* com.example.JobApp.service.*.*(..))")
//        public void logMethodCall() {
//            LOGGER.info("Method Called ");
//        }

    // @Before("execution (* com.example.JobApp.service.JobService.returnOnePost(..)) || execution(* com.example.JobApp.service.JobService.updateJobPost(..))")
    @Before("execution (* com.example.JobApp.service.JobService.*(..))")
    public void logMethodCall(JoinPoint jp) {

        // Log the method name and its arguments
        // Obtaining the Method Name
        String methodName = jp.getSignature().getName();
        // Obtaining the Method Arguments:
        Object[] methodArgs = jp.getArgs();// returns an array of objects representing the arguments passed to the method.

        LOGGER.info("Method Called: " + methodName + " with args " + Arrays.toString(methodArgs));
    }

    @After("execution (* com.example.JobApp.service.JobService.*(..))")
    public void logMethodExecuted(JoinPoint jp) {
        LOGGER.info("Method Executed: " + jp.getSignature().getName());
    }

    @AfterThrowing("execution (* com.example.JobApp.service.JobService.*(..))")
    public void logMethodCrashed(JoinPoint jp) {
        LOGGER.info("Method has some issues: " + jp.getSignature().getName());
    }

    @AfterReturning("execution (* com.example.JobApp.service.JobService.*(..))")
    public void logMethodExecutedSuccess(JoinPoint jp) {
        LOGGER.info("Method Executed Successfully: " + jp.getSignature().getName());
    }

}
