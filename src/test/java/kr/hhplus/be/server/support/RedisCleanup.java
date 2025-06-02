package kr.hhplus.be.server.support;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCleanup {

    private final RedissonClient redisson;

    public void truncate() {
        redisson.getKeys().flushall();
    }
}
