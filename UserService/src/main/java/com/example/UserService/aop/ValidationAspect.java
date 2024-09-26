package com.example.UserService.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

    @Around("execution(* com.example.UserService.service.JobService.returnOnePost(..)) && args(postId)") // pass the args from the method
    public Object validateAndUpdate(ProceedingJoinPoint jp, int postId) throws Throwable {

        if (postId < 0) {
            LOGGER.info("PostId is negative, updating it");

            postId = -postId;

            LOGGER.info("new Value "+postId);
        }

        // return the new args to the method

        return jp.proceed(new Object[] {postId});
    }
}