package kr.hhplus.be.server.domain.redis;

import java.util.List;
import java.util.Optional;

public interface RedisZSetStore<T> {

    List<T> reverseRangeWithScores(String key);
    List<T> reverseRangeWithScores(String key, long start, long end);
    List<T> rangeWithScores(String key);  // 오름차순
    List<T> rangeWithScores(String key, long start, long end);  // 오름차순
    Optional<Double> getScore(String key, String member);
    void remove(String key, String member);
    void incrementScore(String key, String member, double score);
    boolean add(String key, String member, double score);

}
