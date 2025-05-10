package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.interfaces.common.annotation.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
public class CouponLockAspect {

    private final RedissonClient redissonClient;

    public CouponLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(distributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        RLock lock = redissonClient.getLock(key);

        try (CouponLockExecutor executor = new CouponLockExecutor(lock, 3, 10, TimeUnit.SECONDS)) {
            return joinPoint.proceed();
        }
    }
}
