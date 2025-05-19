package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.SalesStatRepository;
import kr.hhplus.be.server.domain.salesStat.SalesStatService;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportCommand;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportService;
import kr.hhplus.be.server.interfaces.scheduler.salesStat.SalesStatScheduler;
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
    SalesReportService salesReportService;

    @Autowired
    SalesStatService salesStatService;

    @Autowired
    SalesStatRepository salesStatRepository;

    private final LocalDate targetDate = LocalDate.now().minusDays(1);

    @BeforeEach
    void setUp() {
        // ZADD products:dailySalesReport:{yyyyMMdd} 10 101, 5 102
        salesReportService.increaseDailySalesReport(SalesReportCommand.IncreaseSalesReport.of(targetDate, 101L, 10.0));
        salesReportService.increaseDailySalesReport(SalesReportCommand.IncreaseSalesReport.of(targetDate, 102L, 20.0));
    }

    @AfterEach
    void tearDown() {

        salesReportService.deleteByLocalDate(SalesReportCommand.ReportDate.of(targetDate));
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
