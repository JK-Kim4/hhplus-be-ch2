package kr.hhplus.be.server.interfaces.common.lock;

public interface LockExecutor {
    LockExecutorType getType();
    <T> T execute(String key, long waitTime, long leaseTime, DistributedLockCallback<T> action);
}
