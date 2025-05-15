package kr.hhplus.be.server.domain.redis;

import java.time.Duration;

public interface RedisCommonStore {

    boolean hasKey(String key);
    void removeWithKey(String key);
    void setExpireTtl(String key, Duration duration);
    Long getExpireTtl(String key);

}
