package kr.hhplus.be.server.common.lock;

import kr.hhplus.be.server.application.lock.LockConfig;

public interface LockExecutor {

    LockExecutorType getType();

    <T> T execute(String lockKey, LockCallBack<T> task, LockConfig config);
}
