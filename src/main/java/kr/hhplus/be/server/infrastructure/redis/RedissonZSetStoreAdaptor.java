package kr.hhplus.be.server.infrastructure.redis;

import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class RedissonZSetStoreAdaptor implements RedisZSetStore<TypedScore> {

    private final RedissonClient redissonClient;

    public RedissonZSetStoreAdaptor(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    @Override
    public List<TypedScore> reverseRangeWithScores(String key) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);

        return zset.entryRange(0, -1).stream()
                .map(entry
                        -> new TypedScore(entry.getValue(), entry.getScore()))
                .sorted(Comparator.comparingDouble(TypedScore::score).reversed())
                .toList();
    }

    @Override
    public List<TypedScore> reverseRangeWithScores(String key, long start, long end) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);

        return zset.entryRange((int) start, (int) end).stream()
                .map(entry
                        -> new TypedScore(entry.getValue(), entry.getScore()))
                .sorted(Comparator.comparingDouble(TypedScore::score).reversed())
                .toList();
    }

    @Override
    public List<TypedScore> rangeWithScores(String key) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);

        return zset.entryRange(0, -1).stream()
                .map(entry
                        -> new TypedScore(entry.getValue(), entry.getScore()))
                .toList();
    }

    @Override
    public Optional<Double> getScore(String key, String member) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        Double score = zset.getScore(member);
        return Optional.ofNullable(score);
    }

    @Override
    public void remove(String key, String member) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        zset.remove(member);
    }

    @Override
    public void incrementScore(String key, String member, double score) {
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
        zset.addScore(member, score);
    }
}
