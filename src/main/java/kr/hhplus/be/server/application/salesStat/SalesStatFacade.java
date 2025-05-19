package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import kr.hhplus.be.server.domain.salesStat.SalesStatService;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReport;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportCommand;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class SalesStatFacade {

    private final SalesStatService salesStatService;
    private final SalesReportService salesReportService;

    public SalesStatFacade(
            SalesStatService salesStatService,
            SalesReportService salesReportService) {
        this.salesStatService = salesStatService;
        this.salesReportService = salesReportService;
    }

    public void persistRedisSalesStat(){
        List<SalesReport> reports =
                salesReportService.findAllByReportDate(SalesReportCommand.ReportDate.of(LocalDate.now().minusDays(1)));

        salesStatService.createAll(SalesStatCommand.Creates.fromReportList(reports));
    }


}
