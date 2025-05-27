package kr.hhplus.be.server.infrastructure.history.exception;

import kr.hhplus.be.server.common.keys.RedisKeys;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryRepository;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public class ExceptionHistoryRepositoryImpl implements ExceptionHistoryRepository {

    private final RedissonClient redissonClient;

    public ExceptionHistoryRepositoryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void save(LocalDate exceptionDate, ExceptionHistory exceptionHistory) {
        String key = RedisKeys.EXCEPTION_HISTORY.format(exceptionDate);
        redissonClient.getSet(key).add(exceptionHistory);
    }

    @Override
    public Set<ExceptionHistory> findByDate(LocalDate exceptionDate) {
        String key = RedisKeys.EXCEPTION_HISTORY.format(exceptionDate);
        return redissonClient.getSet(key);
    }
}
