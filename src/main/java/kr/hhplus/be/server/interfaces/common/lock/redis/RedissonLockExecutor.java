package kr.hhplus.be.server.interfaces.common.lock.redis;

import kr.hhplus.be.server.interfaces.common.exception.DistributedLockException;
import kr.hhplus.be.server.interfaces.common.lock.DistributedLockCallback;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutor;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedissonLockExecutor implements LockExecutor {

    private final RedissonClient redissonClient;

    public RedissonLockExecutor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public LockExecutorType getType() {
        return LockExecutorType.REDISSON;
    }

    @Override
    public <T> T execute(String key, long waitTime, long leaseTime, DistributedLockCallback<T> action) {
        RLock lock = redissonClient.getLock(key);
        try {
            return tryExecuteWithLock(lock, key, waitTime, leaseTime, action);
        } finally {
            unlockSafely(lock);
        }
    }

    private <T> T tryExecuteWithLock(RLock lock, String key, long waitTime, long leaseTime, DistributedLockCallback<T> action) {
        boolean locked;
        try {
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!locked) {
                throw new CannotAcquireLockException("락 획득 실패: " + key);
            }
            return action.call();
        } catch (Exception e) {
            throw new RuntimeException("락 수행 중 예외 발생", e);
        } catch (Throwable t) {
            throw new DistributedLockException("Unexpected error during lock execution", t);
        }
    }

    private void unlockSafely(RLock lock) {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    return;
                }
                return; // 이미 락이 없으면 성공
            } catch (Exception e) {
                log.warn("🚨 락 해제 시도 {}회 실패: {}%n", i + 1, e.getMessage());
                try {
                    Thread.sleep(100); // 짧은 대기 후 재시도
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        log.error("❌ 락 해제 3회 모두 실패 - 수동 조치 필요");
    }
}
