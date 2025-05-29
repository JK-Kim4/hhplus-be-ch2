package kr.hhplus.be.server.infrastructure.lock;

import kr.hhplus.be.server.common.TransactionSynchronizer;
import kr.hhplus.be.server.common.executor.LockExecutor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PubSubLockExecutor implements LockExecutor {

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
