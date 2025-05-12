package kr.hhplus.be.server.application.coupon;

import org.redisson.api.RLock;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

public class CouponLockExecutor implements AutoCloseable{

    private final RLock lock;
    private final boolean isLocked;

    public CouponLockExecutor(RLock lock, long waitTime, long leaseTime, TimeUnit unit) {
        this.lock = lock;
        try {
            this.isLocked = lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Lock acquisition interrupted", e);
        }

        if (!isLocked) {
            throw new IllegalStateException("Failed to acquire lock");
        }

        // 트랜잭션 종료 시점에 락 해제 등록
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    lock.unlock();
                }
            });
        } else {
            // 트랜잭션이 없을 경우에도 반드시 해제
            Runtime.getRuntime().addShutdownHook(new Thread(lock::unlock));
        }
    }


    @Override
    public void close() throws Exception {
        // 트랜잭션이 없다면 즉시 해제
        if (!TransactionSynchronizationManager.isSynchronizationActive() && isLocked) {
            lock.unlock();
        }
    }
}
