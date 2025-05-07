package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import kr.hhplus.be.server.domain.salesStat.SalesStatInfo;
import kr.hhplus.be.server.domain.salesStat.SalesStatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SalesStatScheduler {

    private final SalesStatService salesStatService;

    public SalesStatScheduler(SalesStatService salesStatService) {
        this.salesStatService = salesStatService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void calculateSalesStat(){
        LocalDate targetDate = LocalDate.now().minusDays(1);

        SalesStatInfo.SalesStats salesStatsByDate =
                salesStatService.getSalesStatsByDate(SalesStatCommand.SalesStats.of(targetDate));

        salesStatService.createAll(salesStatsByDate.toCreatesCommand());
    }
}
