package kr.hhplus.be.server.infrastructure.coupon.event;

import kr.hhplus.be.server.domain.coupon.CouponEventInMemoryRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class CouponEventRedisRepository implements CouponEventInMemoryRepository {

    private final RedissonClient redissonClient;
    public CouponEventRedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void saveIdempotencyKey(String value) {
        RBucket<String> bucket = redissonClient.getBucket(value);
        bucket.set(value);
    }

    @Override
    public boolean hasIdempotencyKey(String value) {
        return false;
    }
}
