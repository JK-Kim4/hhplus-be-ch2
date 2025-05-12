package kr.hhplus.be.server.infrastructure.lock;

import kr.hhplus.be.server.common.exception.DistributedLockException;
import kr.hhplus.be.server.common.lock.LockManager;
import org.redisson.api.RLock;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;

public class PubSubLockManager implements LockManager {

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
                    System.out.println("LOCK 획득 [Thread name: " + Thread.currentThread().getName() + "]");
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

    private void unlockIfHeld() {
        if (isLocked && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // 트랜잭션이 없다면(종료 포함) 즉시 해제
        if (!TransactionSynchronizationManager.isSynchronizationActive())  {
            System.out.println("LOCK 반납 [Thread name: " + Thread.currentThread().getName() + "]");
            unlockIfHeld();
            return;
        }

        throw new DistributedLockException("트랜잭션 종료 이전에 Lock을 해제할 수 없습니다.");
    }

    public boolean isLocked() {
        return isLocked;
    }
}
