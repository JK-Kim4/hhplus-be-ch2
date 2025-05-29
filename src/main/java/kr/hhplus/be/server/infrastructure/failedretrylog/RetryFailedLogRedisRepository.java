package kr.hhplus.be.server.infrastructure.failedretrylog;

import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLogRepository;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RetryFailedLogRedisRepository implements RetryFailedLogRepository {

    private final RedissonClient redissonClient;
    public RetryFailedLogRedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void save(String key, String json) {
        redissonClient.getList(key).add(json);
    }

    @Override
    public void clear(String key) {
        redissonClient.getList(key).clear();
    }

    @Override
    public List<String> findAllRowJsonByKey(String key) {
        RList<String> list = redissonClient.getList(key);
        return list.readAll();
    }
}
