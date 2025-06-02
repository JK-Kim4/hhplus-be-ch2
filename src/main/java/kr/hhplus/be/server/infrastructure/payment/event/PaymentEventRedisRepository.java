package kr.hhplus.be.server.infrastructure.payment.event;

import kr.hhplus.be.server.domain.payment.PaymentEventInMemoryRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentEventRedisRepository implements PaymentEventInMemoryRepository {

    private final RedissonClient redissonClient;

    public PaymentEventRedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void saveIdempotencyKey(String value) {
        RBucket<String> bucket = redissonClient.getBucket(value);
        bucket.set(value);
    }

    @Override
    public boolean hasIdempotencyKey(String value) {
        return redissonClient.getBucket(value).isExists();
    }
}
