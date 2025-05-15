package kr.hhplus.be.server.infrastructure.redis.redisson;

import kr.hhplus.be.server.domain.redis.RedisMapStore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class RedissonMapStoreAdaptor implements RedisMapStore {

    private final RedissonClient redissonClient;

    public RedissonMapStoreAdaptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void put(Long key, Long value, Long timeMillis){
        redissonClient.getMap(String.valueOf(key)).fastPut(value, timeMillis);
    }

    @Override
    public void remove(String key) {

    }
}
