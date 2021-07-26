package com.wby.weather.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    private static final ConcurrentMap<String, com.google.common.util.concurrent.RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.wby.weather.common.RateLimiter)")
    public void rateLimit() {

    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null && rateLimiter.qps() > RateLimiter.NOT_LIMITED) {
            double qps = rateLimiter.qps();
            if (RATE_LIMITER_CACHE.get(method.getName()) == null) {
                RATE_LIMITER_CACHE.put(method.getName(), com.google.common.util.concurrent.RateLimiter.create(qps));
            }

            log.debug("【{}】 set QPS: {}", method.getName(), RATE_LIMITER_CACHE.get(method.getName()).getRate());
            if (RATE_LIMITER_CACHE.get(method.getName()) != null && !RATE_LIMITER_CACHE.get(method.getName()).tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                throw new RuntimeException("do not so quick");
            }
        }
        return point.proceed();
    }
}

