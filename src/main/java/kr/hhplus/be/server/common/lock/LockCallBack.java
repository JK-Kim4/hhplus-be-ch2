package kr.hhplus.be.server.common.lock;

@FunctionalInterface
public interface LockCallBack<T> {
    T call() throws Throwable;
}
