package kr.hhplus.be.server.support;

import kr.hhplus.be.server.infrastructure.lock.LockConfig;
import kr.hhplus.be.server.infrastructure.lock.PubSubLockManager;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public class LockManagerSupporter {

    public static PubSubLockManager 기본_LOCK_MANAGER_생성(RLock lock){
        return new PubSubLockManager(lock,
                new LockConfig(1, 3, TimeUnit.SECONDS, 1, 100));
    }
}
