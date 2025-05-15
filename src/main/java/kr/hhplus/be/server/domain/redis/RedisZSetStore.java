package kr.hhplus.be.server.domain.redis;

import kr.hhplus.be.server.domain.salesStat.TypedScore;

import java.util.List;
import java.util.Optional;

public interface RedisZSetStore<T> {

    List<TypedScore<String>> reverseRangeWithScores(String key);
    List<TypedScore<String>> reverseRangeWithScores(String key, long start, long end);
    List<TypedScore<String>> rangeWithScores(String key);  // 오름차순
    Optional<Double> getScore(String key, String member);
    void remove(String key, String member);
    void incrementScore(String key, String member, double score);

}
