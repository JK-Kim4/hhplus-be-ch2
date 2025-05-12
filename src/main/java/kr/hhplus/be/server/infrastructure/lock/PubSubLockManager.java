package kr.hhplus.be.server.infrastructure.lock;

import kr.hhplus.be.server.common.exception.DistributedLockException;
import kr.hhplus.be.server.common.lock.LockManager;
import org.redisson.api.RLock;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PubSubLockManager implements LockManager {

    private final RLock lock;
    private final boolean isLocked;

    public PubSubLockManager(RLock rlock, LockConfig lockConfig){
        this.lock = rlock;
        this.isLocked = tryAcquireLockWithRetry(
                lockConfig.waitTime(),
                lockConfig.leaseTime(),
                lockConfig.timeUnit(),
                lockConfig.mayRetry(),
                lockConfig.retryIntervalMillis());
        registerReleaseHook();

    }

    private boolean tryAcquireLockWithRetry(long waitTime, long leaseTime, TimeUnit unit, int maxRetry, long retryIntervalMillis) {
        int retryCount = 0;

        while (retryCount <= maxRetry) {
            try {
                boolean acquired = lock.tryLock(waitTime, leaseTime, unit);
                if (acquired) {
                    System.out.println("LOCK 획득 [Thread name: " + Thread.currentThread().getName() + "]");
                    return true;
                }
                retryCount++;
                Thread.sleep(retryIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DistributedLockException("Lock acquisition interrupted", e);
            }
        }

        //락 획득 최종 실패 경우 오류 발생
        throw new DistributedLockException("Lock 획득에 실패하였습니다. (총 시도 횟수: %d회)".formatted(retryCount));
    }

    //락 해제 시점 등록
    private void registerReleaseHook() {

        /*
         * TransactionSynchronizationManager
         * 현재 스레드가 트랜잭션 컨텍스트 내에서 실행 중인지 확인하거나,
         * 트랜잭션 커밋/롤백 후 특정 작업을 등록할 수 있게 도와주는 유틸리티입니다.
         *
         * isSynchronizationActive(): 현재 스레드가 트랜잭션 컨텍스트 내에 있는지를 확인
         * registerSynchronization( TransactionSynchronizationAdapter afterCompletion()): afterCompletion(int status)는 트랜잭션이 commit 또는 rollback된 직후 실행됩니다.
         * */
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    unlockIfHeld();
                }
            });
        } else {
            Runtime.getRuntime().addShutdownHook(new Thread(this::unlockIfHeld));
        }
    }

    private void unlockIfHeld() {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        // 트랜잭션이 없다면(종료 포함) 즉시 해제
        if (!TransactionSynchronizationManager.isSynchronizationActive()
                && isLocked
                && lock.isHeldByCurrentThread())  {
            System.out.println("LOCK 반납 [Thread name: " + Thread.currentThread().getName() + "]");
            lock.unlock();
            return;
        }

        throw new DistributedLockException("트랜잭션 종료 이전에 Lock을 해제할 수 없습니다.");
    }

    public boolean isLocked() {
        return isLocked;
    }
}
