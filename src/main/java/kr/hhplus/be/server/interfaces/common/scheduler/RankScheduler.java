package kr.hhplus.be.server.interfaces.common.scheduler;

import kr.hhplus.be.server.application.item.ItemFacade;
import kr.hhplus.be.server.domain.rank.Rank;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RankScheduler {

    private final ItemFacade itemFacade;

    public RankScheduler(
            ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void orderStatistic() {
        List<Rank> rankList = itemFacade.createRankList(LocalDate.now().minusDays(1));
        itemFacade.saveRankList(rankList);
    }


}
