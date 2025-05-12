package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.lock.*;
import kr.hhplus.be.server.infrastructure.lock.LockConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(0)
@Aspect
@Component
public class DistributedLockAspect {

    private final LockKeyGenerator keyGenerator;
    private final LockExecutorRegistry lockExecutorRegistry;

    public DistributedLockAspect(
            LockKeyGenerator keyGenerator,
            LockExecutorRegistry lockExecutorRegistry) {
        this.keyGenerator = keyGenerator;
        this.lockExecutorRegistry = lockExecutorRegistry;
    }

    @Around("@annotation(distributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = keyGenerator.generateKey(joinPoint, distributedLock.prefix(), distributedLock.key());
        LockExecutor executorV2 = getLockExecutor(distributedLock.executor());
        return executeWithLock(executorV2, key, distributedLock, joinPoint);
    }

    private LockExecutor getLockExecutor(LockExecutorType type) {
        return lockExecutorRegistry.get(type);
    }

    private Object executeWithLock(LockExecutor executorV2, String key, DistributedLock distributedLock, ProceedingJoinPoint joinPoint) throws Throwable {
        return executorV2.execute(key, joinPoint::proceed, new LockConfig(
                distributedLock.waitTime(),
                distributedLock.leaseTime(),
                distributedLock.timeUnit(),
                distributedLock.maxRetry(),
                distributedLock.retryIntervalMillis()));
    }
}
