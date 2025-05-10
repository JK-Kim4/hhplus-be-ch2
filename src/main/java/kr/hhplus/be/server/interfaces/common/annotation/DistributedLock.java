package kr.hhplus.be.server.interfaces.common.annotation;

import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 락 키의 접두사 (예: "order")
     */
    String prefix() default "";
    /**
     * SpEL 식으로 동적으로 생성할 락 키. 예: "#userId", "#order.id"
     */
    String key();
    long waitTime() default 5L;
    long leaseTime() default 30L;

    LockExecutorType executor() default LockExecutorType.REDISSON;
}
