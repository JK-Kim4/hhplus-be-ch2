package kr.hhplus.be.server.infrastructure.lock;

import kr.hhplus.be.server.common.TransactionSynchronizer;
import kr.hhplus.be.server.common.exception.DistributedLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class PubSubLockManager implements LockManager {

    private final Logger logger = LoggerFactory.getLogger(PubSubLockManager.class);
    private final RLock lock;
    private final boolean isLocked;

    public PubSubLockManager(RLock rlock, LockConfig lockConfig){
        this.lock = rlock;
        this.isLocked = tryAcquireLockWithRetry(lockConfig);

    }

    private boolean tryAcquireLockWithRetry(LockConfig lockConfig) {
        int retryCount = 0;

        while (retryCount <= lockConfig.mayRetry()) {
            try {
                boolean acquired = lock.tryLock(lockConfig.waitTime(), lockConfig.leaseTime(), lockConfig.timeUnit());
                if (acquired) {
                    logger.info("LOCK 획득 [Thread name: {}]", Thread.currentThread().getName());
                    return true;
                }
                retryCount++;
                Thread.sleep(lockConfig.retryIntervalMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DistributedLockException("Lock acquisition interrupted", e);
            }
        }

        //락 획득 최종 실패 경우 오류 발생
        throw new DistributedLockException("Lock 획득에 실패하였습니다. (총 시도 횟수: %d회)".formatted(retryCount));
    }

    @Override
    public void close() throws IOException {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Component
    public static class PubSubLockExecutor implements LockCallBack.LockExecutor {

        private final Logger logger = LoggerFactory.getLogger(PubSubLockExecutor.class);

        private final RedissonClient redissonClient;

        public PubSubLockExecutor(
                RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }

        @Override
        public LockExecutorType getType() {
            return LockExecutorType.PUBSUB;
        }

        @Override
        public <T> T execute(String lockKey, LockCallBack<T> task, LockConfig lockConfig) {
            RLock rLock = redissonClient.getLock(lockKey);
            try (PubSubLockManager manager = new PubSubLockManager(rLock, lockConfig)) {
                return task.call();
            } catch (Throwable e) {
                throw new RuntimeException("Failed to execute locked task", e);
            } finally {
                TransactionSynchronizer.runAfterCommit(() -> unlockIfHeld(rLock) );
            }
        }

        private void unlockIfHeld(RLock lock) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
