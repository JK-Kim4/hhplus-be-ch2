package kr.hhplus.be.server.integration.redis;

import kr.hhplus.be.server.infrastructure.lock.PubSubLockManager;
import kr.hhplus.be.server.support.LockManagerSupport;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class RedisDistributedLockTest {

    @Autowired
    private RedissonClient redissonClient;


    @Test
    void 락_획득에_성공하면_로직이_정상_실행된다() throws IOException {
        String lockKey = "test:integration:success";
        RLock lock = redissonClient.getLock(lockKey);

        try(PubSubLockManager manager = LockManagerSupport.기본_LOCK_MANAGER_생성(lock)) {
            assertTrue(manager.isLocked());
        }
    }

    @Test
    void 락_획득에_실패하면_예외가_발생한다() throws InterruptedException {
        String lockKey = "test:integration:fail";
        RLock lock = redissonClient.getLock(lockKey);

        // 다른 스레드에서 먼저 락 선점
        lock.lock(5, TimeUnit.SECONDS);

        try {
            assertThrows(RuntimeException.class, () -> {
                try (PubSubLockManager manager = LockManagerSupport.기본_LOCK_MANAGER_생성(lock)) {
                    // 이 블록이 실행되면 안 됨
                    throw new IllegalStateException("실행되면 안됨");
                }
            });
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Test
    void leaseTime_이후에는_락이_자동으로_풀려야_한다() throws Exception {
        String lockKey = "test:integration:ttl";
        RLock lock = redissonClient.getLock(lockKey);
        try (PubSubLockManager manager = LockManagerSupport.기본_LOCK_MANAGER_생성(lock)) {
            System.out.println("[1] 락 획득 및 유지 중");
            Thread.sleep(1000);
        }

        // leaseTime이 지나도록 기다림
        Thread.sleep(2500);

        // 다시 락을 시도
        try (PubSubLockManager manager = LockManagerSupport.기본_LOCK_MANAGER_생성(lock)) {
            //Lock 해제 시 획득 성공
            assertTrue(manager.isLocked());
        }
    }


}
