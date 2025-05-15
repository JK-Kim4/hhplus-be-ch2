package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.application.salesStat.SalesStatProcessor;
import kr.hhplus.be.server.domain.salesStat.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class SalesStatScheduler {


    private final RedisSalesStatService redisSalesStatService;
    private final SalesStatService salesStatService;

    public SalesStatScheduler(
            RedisSalesStatService redisSalesStatService,
            SalesStatService salesStatService) {
        this.redisSalesStatService = redisSalesStatService;
        this.salesStatService = salesStatService;
    }

    //매일 자정 당일 판매 집계 영속화, 금일 판매 집계 redis key 생성
    @Scheduled(cron = "0 0 0 * * *")
    public void persistDailySalesStatsAtMidnight(){
        LocalDate targetDate = LocalDate.now().minusDays(1);
        String targetKey = SalesStatProcessor.getDailySalesReportKey(targetDate);

        SalesStatInfo.RedisTypedScoreSet typedScoreSet = redisSalesStatService.findReverseRangeWithScoresSetByKey(targetKey);

        List<SalesStatInfo.SalesReport> list = salesStatService.getSalesReportsFromRedisTypedScoreSet(typedScoreSet,targetDate);

        salesStatService.createAll(SalesStatCommand.Creates.fromList(list));
    }
}
