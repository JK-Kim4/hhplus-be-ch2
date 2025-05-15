package kr.hhplus.be.server.domain.redis;

import java.time.Duration;

public interface RedisCommonStore {

    boolean hasKey(String key);
    void removeWithKey(String key);
    void removeWithKeys(String... keys);
    void setExpireTtl(String key, Duration duration);
    Long getExpireTtl(String key);
    void writeSimpleKey(String  key);
    void getAtomicLong(String key);
}
