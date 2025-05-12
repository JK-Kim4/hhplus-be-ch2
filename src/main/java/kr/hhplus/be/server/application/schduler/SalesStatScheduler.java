package kr.hhplus.be.server.application.schduler;

import kr.hhplus.be.server.application.salesStat.SalesStatCriteria;
import kr.hhplus.be.server.application.salesStat.SalesStatFacade;
import kr.hhplus.be.server.application.salesStat.SalesStatResult;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.salesStat.SalesStatService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Transactional
public class SalesStatScheduler {

    private final SalesStatService salesStatService;
    private final SalesStatFacade salesStatFacade;
    private final ProductService productService;

    public SalesStatScheduler(SalesStatService salesStatService,
                              SalesStatFacade salesStatFacade,
                              ProductService productService) {
        this.salesStatService = salesStatService;
        this.salesStatFacade = salesStatFacade;
        this.productService = productService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @CacheEvict(value = "products:rank", allEntries = true) // 또는 key 지정
    public void calculateSalesStat(){
        LocalDate targetDate = LocalDate.now().minusDays(1);

        SalesStatResult.SalesStats salesStats =
                salesStatFacade.getSalesStatsByDate(SalesStatCriteria.TargetDate.of(targetDate));

        salesStatService.createAll(salesStats.toCreatesCommand());

        setRankDefaultCache();
    }

    private void setRankDefaultCache(){
        // 캐시 warm-up: 조회 메소드 호출 → @Cacheable로 인해 새로 캐싱됨
        productService.findRankWithLimit(3);
        productService.findRankWithLimit(10);
        productService.findRankWithLimit(20);
        productService.findRankWithLimit(50);
    }
}
