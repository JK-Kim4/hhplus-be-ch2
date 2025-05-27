package kr.hhplus.be.server.interfaces.salesstat.scheduler;

import kr.hhplus.be.server.application.salesstat.SalesStatFacade;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile({"prod", "test"})
@Transactional
public class SalesStatScheduler {

    private final SalesStatFacade salesStatFacade;

    public SalesStatScheduler(
            SalesStatFacade salesStatFacade) {
        this.salesStatFacade = salesStatFacade;
    }

    //매일 자정 당일 판매 집계 영속화, 금일 판매 집계 redis key 생성
    @Scheduled(cron = "0 0 0 * * *")
    public void persistDailySalesStatsAtMidnight(){
        salesStatFacade.persistRedisSalesStat();
    }
}
