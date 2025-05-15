package kr.hhplus.be.server.domain.salesStat;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisSalesStatService {

    private final RedisZSetStore<String> redisZSetStore;
    private final RedisCommonStore redisCommonStore;

    public RedisSalesStatService(
            RedisZSetStore redisZSetStore,
            RedisCommonStore redisCommonStore) {
        this.redisZSetStore = redisZSetStore;
        this.redisCommonStore = redisCommonStore;
    }

    public void incrementZSetScoreByKeyWithMember(SalesStatCommand.RedisAddSortedSet command){
        redisZSetStore.incrementScore(command.getKey(), command.getValue(), command.getScore());

    }

    public void removeByKey(SalesStatCommand.RedisDeleteKey command){
        redisCommonStore.removeWithKey(command.getKey());
    }

    public void removeZSetMemberByKey(String key, String member){
        redisZSetStore.remove(key, member);
    }

    public void setExpireTtl(String key, Duration duration) {
        redisCommonStore.setExpireTtl(key, duration);
    }

    public Long getExpireTtl(String key) {
        return redisCommonStore.getExpireTtl(key);
    }

    public SalesStatInfo.RedisTypedScoreSet findReverseRangeWithScoresSetByKey(String key){
        return SalesStatInfo.RedisTypedScoreSet.of(redisZSetStore.reverseRangeWithScores(key));
    }

    public SalesStatInfo.RedisTypedScoreSet findRangeWithScoresByKey(String key){
        return SalesStatInfo.RedisTypedScoreSet.of(redisZSetStore.rangeWithScores(key));
    }

    public boolean hasKey(String key){
        return redisCommonStore.hasKey(key);
    }

    public Double findSZetScoreByKeyAndMember(String key, String member) {
        return redisZSetStore.getScore(key, member).orElseThrow(NoResultException::new);
    }

}
