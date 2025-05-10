package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.interfaces.common.lock.redis.RedissonLockExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class RedissonLockExecutorTest {

    RedissonClient redissonClient;
    RLock rLock;
    RedissonLockExecutor executor;

    @BeforeEach
    void setup() {
        redissonClient = mock(RedissonClient.class);
        rLock = mock(RLock.class);
        executor = new RedissonLockExecutor(redissonClient);

        given(redissonClient.getLock("lock:key")).willReturn(rLock);
    }

    @Test
    void tryLock_락획득_요청_성공할경우_로직이_실행된다() throws Exception {
        given(rLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
        given(rLock.isHeldByCurrentThread()).willReturn(true);

        String result = executor.execute("lock:key", 1, 3, () -> "success");

        assertEquals("success", result);
        then(rLock).should().unlock();
    }

    @Test
    void tryLock_락획득_요청_실패할경우_오류_반환() throws Exception {
        given(rLock.tryLock(anyLong(), anyLong(), any())).willReturn(false);

        assertThrows(RuntimeException.class, () ->
                executor.execute("lock:key", 1, 3, () -> "fail")
        );
    }

}
