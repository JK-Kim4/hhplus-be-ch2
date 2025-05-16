package kr.hhplus.be.server.infrastructure.lock;

import kr.hhplus.be.server.common.exception.DistributedLockException;
import kr.hhplus.be.server.common.lock.LockManager;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
