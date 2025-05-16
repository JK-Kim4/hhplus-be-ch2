package kr.hhplus.be.server.infrastructure.redis;

import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedissonCommonStoreAdaptor implements RedisCommonStore {

    private final RedissonClient redissonClient;

    public RedissonCommonStoreAdaptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean hasKey(String key) {
        return redissonClient.getKeys().countExists(key) > 0;
    }

    @Override
    public void removeWithKey(String key) {
        redissonClient.getKeys().delete(key);
    }

    @Override
    public void setExpireTtl(String key, Duration duration) {
        // TTL 설정은 키가 어떤 자료형이든 상관없이 getBucket을 사용하면 대부분 OK
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.expire(duration);
    }

    @Override
    public Long getExpireTtl(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.remainTimeToLive();
    }
}
