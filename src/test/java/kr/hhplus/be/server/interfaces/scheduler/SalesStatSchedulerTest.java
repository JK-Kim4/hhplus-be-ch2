package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.application.salesStat.SalesStatProcessor;
import kr.hhplus.be.server.domain.salesStat.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class SalesStatSchedulerTest {

    @Autowired
    SalesStatScheduler salesStatScheduler;

    @Autowired
    RedisSalesStatService redisSalesStatService;

    @Autowired
    SalesStatService salesStatService;

    @Autowired
    SalesStatRepository salesStatRepository;

    private final LocalDate targetDate = LocalDate.now().minusDays(1);
    private final String redisKey = SalesStatProcessor.getDailySalesReportKey(targetDate);

    @BeforeEach
    void setUp() {
        // ZADD products:dailySalesReport:{yyyyMMdd} 10 101, 5 102
        redisSalesStatService.incrementZSetScoreByKeyWithMember(SalesStatCommand.RedisAddSortedSet.of(redisKey, "101", 10));
        redisSalesStatService.incrementZSetScoreByKeyWithMember(SalesStatCommand.RedisAddSortedSet.of(redisKey, "102", 20));
    }

    @AfterEach
    void tearDown() {
        redisSalesStatService.removeByKey(SalesStatCommand.RedisDeleteKey.of(redisKey));
    }


    @Test
    void REDIS_당일_판매집계정보를_DB에_영속화한다() {
        // when
        salesStatScheduler.persistDailySalesStatsAtMidnight();
        List<SalesStat> stats = salesStatRepository.findAll();
        Map<Long, Long> expected = Map.of(
                101L, 10L,
                102L, 20L
        );


        // then
        assertEquals(2, stats.size());
        stats.stream()
                .forEach(stat -> {
                    assertEquals(targetDate, stat.getSalesDate());
                    assertTrue(expected.containsKey(stat.getProductId()));
                    assertEquals(expected.get(stat.getProductId()), stat.getSalesAmount());
                });
    }

}
