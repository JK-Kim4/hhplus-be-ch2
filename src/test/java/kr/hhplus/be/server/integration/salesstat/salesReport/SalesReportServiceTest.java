package kr.hhplus.be.server.integration.salesstat.salesReport;

import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReport;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReportCommand;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class SalesReportServiceTest {

    @Autowired
    SalesReportService salesReportService;

    @Autowired
    RedissonClient redissonClient;

    @BeforeEach
    void setUp() {
        // Redis 초기화
        redissonClient.getKeys().flushall();
    }

    @Test
    void 판매집계정보를_등록할수있다(){
        //given
        Long member = 1L;
        Double score = 50.0;
        LocalDate targetDate = LocalDate.of(2022, 1, 1);
        SalesReportCommand.IncreaseSalesReport command = SalesReportCommand.IncreaseSalesReport.of(targetDate, member, score);

        //when
        salesReportService.increaseDailySalesReport(command);

        //then
        assertAll("판매집계정보 등록",
                () -> assertTrue(salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(targetDate))),
                () -> assertEquals(score, salesReportService.findByReportDateAndProductId(SalesReportCommand.SalesReport.of(targetDate, member)).getSalesScore())
        );
    }

    @Test
    void existByLocalDate는_파라미터로_전달되는_LocalDate에_해당하는_판매집계의_존재여부를_확인할수있다(){
        //given
        Long member = 1L;
        Double score = 50.0;
        LocalDate targetDate = LocalDate.of(2022, 1, 1);
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(CacheKeys.DAILY_SALES_REPORT.format(targetDate));
        scoredSortedSet.addScore(member, score);

        //when//then
        assertTrue(salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(targetDate)));
    }

    @Test
    void deleteByLocalDate는_파라미터로_전달되는_LocalDate에_해당하는_판매집계를_삭제한다(){
        //given
        Long member = 1L;
        Double score = 50.0;
        LocalDate targetDate = LocalDate.of(2022, 1, 1);
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(CacheKeys.DAILY_SALES_REPORT.format(targetDate));
        scoredSortedSet.addScore(member, score);
        assertTrue(salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(targetDate)));

        //when
        salesReportService.deleteByLocalDate(SalesReportCommand.ReportDate.of(targetDate));

        //then
        assertFalse(salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(targetDate)));
    }

    @Test
    void 판매집계정보의_member에_해당하는_집계정보를_조회할수있다(){
        //given
        Long member = 1L;
        Double score = 50.0;
        LocalDate targetDate = LocalDate.of(2022, 1, 1);
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(CacheKeys.DAILY_SALES_REPORT.format(targetDate));
        scoredSortedSet.addScore(member, score);
        SalesReportCommand.SalesReport command = SalesReportCommand.SalesReport.of(targetDate, member);
        assertTrue(salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(targetDate)));

        //when
        SalesReport result = salesReportService.findByReportDateAndProductId(command);

        //then
        assertEquals(member, result.getProductId());
        assertEquals(score, result.getSalesScore());
    }




}
