package kr.hhplus.be.server.infrastructure.redis.redisson;

import kr.hhplus.be.server.domain.redis.RedisSetStore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class RedissonSetStoreAdaptor implements RedisSetStore {

    private final RedissonClient redissonClient;

    public RedissonSetStoreAdaptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void add(String key, String value) {
        redissonClient.getSet(key).add(value);
    }

    @Override
    public void remove(String key, String value) {

    }
}
