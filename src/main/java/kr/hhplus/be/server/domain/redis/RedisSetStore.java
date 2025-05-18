package kr.hhplus.be.server.domain.redis;

public interface RedisSetStore {

    void add(String key, String value);
    void remove(String key, String value);
}
