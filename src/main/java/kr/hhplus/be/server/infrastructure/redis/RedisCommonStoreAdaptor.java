package kr.hhplus.be.server.infrastructure.redis;

import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

//@Repository
public class RedisCommonStoreAdaptor implements RedisCommonStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisCommonStoreAdaptor(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void removeWithKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void setExpireTtl(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    @Override
    public Long getExpireTtl(String key) {
        return redisTemplate.getExpire(key);
    }

}
