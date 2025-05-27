package kr.hhplus.be.server.integration.history.exception;

import kr.hhplus.be.server.common.keys.RedisKeys;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryFactory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@Testcontainers
public class ExceptionHistoryServiceTest {


    @Autowired
    ExceptionHistoryService exceptionHistoryService;

    @Autowired
    RedissonClient redissonClient;

    @Test
    void 발생한_예외의_이력을_저장한다(){
        //given
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        ExceptionHistory exceptionHistory = ExceptionHistoryFactory.create(
                "test",
                new String[]{"123", "456"},
                true
        );

        //when
        exceptionHistoryService.saveExceptionHistory(localDate, exceptionHistory);
        Set<ExceptionHistory> set = redissonClient.getSet(RedisKeys.EXCEPTION_HISTORY.format(localDate));

        //then
        Assertions.assertEquals(exceptionHistory, set.iterator().next());
    }


    
}
