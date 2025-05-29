package kr.hhplus.be.server.integration.retryfailedlog;

import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLog;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLogRepository;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class RetryFailedLogServiceTest {

    @Autowired
    RetryFailedLogService retryFailedLogService;

    @Autowired
    RetryFailedLogRepository retryFailedLogRepository;

    @BeforeEach
    void setUp() {
        retryFailedLogRepository.clear(CacheKeys.FAILED_RETRY_LOG.name());
    }


    @Test
    void 실패한_로그를_저장하고_조회한다() {
        // given
        RetryFailedLog log = new RetryFailedLog(
                "methodName",
                "parameterType",
                "parameterJson",
                "exceptionMessage",
                "exceptionClass",
                LocalDateTime.of(2023, 10, 1, 12, 0));

        // when
        retryFailedLogService.save(log);
        List<RetryFailedLog> result = retryFailedLogService.findAll();
        RetryFailedLog retryFailedLog = result.get(0);

        // then
        assertEquals(log, retryFailedLog);
    }
}
