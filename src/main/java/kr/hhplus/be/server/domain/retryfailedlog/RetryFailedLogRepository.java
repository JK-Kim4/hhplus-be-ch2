package kr.hhplus.be.server.domain.retryfailedlog;

import java.util.List;

public interface RetryFailedLogRepository {

    void save(String key, String value);

    void clear(String key);

    List<String> findAllRowJsonByKey(String key);
}
