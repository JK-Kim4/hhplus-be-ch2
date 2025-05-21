package kr.hhplus.be.server.common.executor;


import kr.hhplus.be.server.infrastructure.lock.LockCallBack;
import kr.hhplus.be.server.infrastructure.lock.LockConfig;
import kr.hhplus.be.server.infrastructure.lock.LockExecutorType;

public interface LockExecutor {

    LockExecutorType getType();

    <T> T execute(String lockKey, LockCallBack<T> task, LockConfig config);
}
