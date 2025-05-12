package kr.hhplus.be.server.common.annotation;

import kr.hhplus.be.server.common.lock.LockExecutorType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String prefix() default "";
    String key();
    long waitTime() default 5L;
    long leaseTime() default 10L;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    int maxRetry() default 3;
    long retryIntervalMillis() default 100;
    LockExecutorType executor() default LockExecutorType.REDISSON;
}
