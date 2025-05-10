package kr.hhplus.be.server.interfaces.redis;

import kr.hhplus.be.server.interfaces.common.lock.redis.RedissonLockExecutor;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class RedisDistributedLockTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedissonLockExecutor lockExecutor;


    @Test
    void 락_획득에_성공하면_콜백이_실행된다() {
        // given
        String lockKey = "test:integration:success";

        // when
        String result = lockExecutor.execute(lockKey, 1, 3, () -> "locked success");

        // then
        assertThat(result).isEqualTo("locked success");
    }

    @Test
    void 락_획득에_실패하면_예외가_발생한다() throws InterruptedException {
        // given
        String lockKey = "test:integration:fail";
        RLock lock = redissonClient.getLock(lockKey);

        // 다른 스레드(혹은 현재 스레드)에서 선점
        lock.lock(5, TimeUnit.SECONDS);

        try {
            // when & then
            assertThrows(RuntimeException.class, () -> {
                lockExecutor.execute(lockKey, 1, 3, () -> {
                    throw new IllegalStateException("이 로직은 실행되면 안됨");
                });
            });
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Test
    void leaseTime이_지나면_락이_자동으로_해제되어야_한다() throws Exception {
        // given
        String lockKey = "test:integration:ttl";

        // 락을 2초만 유지
        lockExecutor.execute(lockKey, 1, 2, () -> {
            System.out.println("[1] 락 획득 및 2초간 유지");
            Thread.sleep(1000);
            return null;
        });

        // wait more than lease time
        Thread.sleep(2500);

        // when: 락이 풀렸는지 다시 시도
        String result = lockExecutor.execute(lockKey, 1, 3, () -> "lock acquired after TTL");

        // then
        assertThat(result).isEqualTo("lock acquired after TTL");
    }


}
