package kr.hhplus.be.server.domain.redis;

public interface RedisMapStore {

    void put(Long key, Long value, Long timeMillis);
    void remove(String key);
}
