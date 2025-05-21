package kr.hhplus.be.server.infrastructure.lock;

@FunctionalInterface
public interface LockCallBack<T> {
    T call() throws Throwable;

    interface LockExecutor {

        LockExecutorType getType();

        <T> T execute(String lockKey, LockCallBack<T> task, LockConfig config);
    }
}
