package kr.hhplus.be.server.interfaces.common.aspect;

import kr.hhplus.be.server.interfaces.common.annotation.DistributedLock;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutor;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;
import kr.hhplus.be.server.interfaces.common.lock.LockKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class DistributedLockAspect {

    private final LockKeyGenerator keyGenerator;
    private final Map<LockExecutorType, LockExecutor> executorMap;

    public DistributedLockAspect(
            LockKeyGenerator keyGenerator,
            List<LockExecutor> executors) {
        this.keyGenerator = keyGenerator;
        this.executorMap = executors.stream()
                .collect(Collectors.toMap(LockExecutor::getType, Function.identity()));
    }


    @Around("@annotation(kr.hhplus.be.server.interfaces.common.annotation.DistributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        String key = keyGenerator.generateKey(method, joinPoint.getArgs(), annotation.prefix(), annotation.key());

        LockExecutor executor = executorMap.get(annotation.executor());

        if (executor == null) {
            throw new IllegalStateException("No LockExecutor found for: " + annotation.executor());
        }

        return executor.execute(key, annotation.waitTime(), annotation.leaseTime(), joinPoint::proceed);
    }

}
