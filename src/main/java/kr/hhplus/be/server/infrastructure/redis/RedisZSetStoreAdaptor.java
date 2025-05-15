package kr.hhplus.be.server.infrastructure.redis;

import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class RedisZSetStoreAdaptor implements RedisZSetStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisZSetStoreAdaptor(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<TypedScore<String>> reverseRangeWithScores(String key) {
        Set<ZSetOperations.TypedTuple<String>> redisResults = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, -1);

        if (redisResults == null) {
            return Collections.emptyList();
        }

        return redisResults.stream()
                .map(tuple -> new TypedScore<>(tuple.getValue(), tuple.getScore()))
                .toList();
    }

    @Override
    public List<TypedScore<String>> reverseRangeWithScores(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<String>> redisResults = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, start, end);

        if (redisResults == null) {
            return Collections.emptyList();
        }

        return redisResults.stream()
                .map(tuple -> new TypedScore<>(tuple.getValue(), tuple.getScore()))
                .toList();
    }

    @Override
    public List<TypedScore<String>> rangeWithScores(String key) {
        Set<ZSetOperations.TypedTuple<String>> redisResults = redisTemplate.opsForZSet()
                .rangeWithScores(key, 0, -1);

        if (redisResults == null) {
            return Collections.emptyList();
        }

        return redisResults.stream()
                .map(tuple -> new TypedScore<>(tuple.getValue(), tuple.getScore()))
                .toList();
    }

    @Override
    public Optional<Double> getScore(String key, String member) {
        return Optional.ofNullable(redisTemplate.opsForZSet().score(key, member));
    }

    @Override
    public void remove(String key, String member) {
        redisTemplate.opsForZSet().remove(key, member);
    }

    @Override
    public void incrementScore(String key, String member, double score) {
        redisTemplate.opsForZSet().incrementScore(key, member, score);
    }
}
