package kr.hhplus.be.server.interfaces.common.lock;

@FunctionalInterface
public interface DistributedLockCallback<T> {
    T call() throws Throwable;
}
