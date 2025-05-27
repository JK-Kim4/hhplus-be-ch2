package kr.hhplus.be.server.application.salesstat;

import kr.hhplus.be.server.common.keys.RedisKeys;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.salesstat.SalesStatCommand;
import kr.hhplus.be.server.domain.salesstat.SalesStatService;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReport;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReportCommand;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReportService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class SalesStatFacade {

    private final SalesStatService salesStatService;
    private final SalesReportService salesReportService;
    private final OrderService orderService;

    public SalesStatFacade(
            SalesStatService salesStatService,
            SalesReportService salesReportService,
            OrderService orderService) {
        this.salesStatService = salesStatService;
        this.salesReportService = salesReportService;
        this.orderService = orderService;
    }

    public void persistRedisSalesStat(){
        List<SalesReport> reports =
                salesReportService.findAllByReportDate(SalesReportCommand.ReportDate.of(LocalDate.now().minusDays(1)));

        salesStatService.createAll(SalesStatCommand.Creates.fromReportList(reports));
    }

    public void dailySalesReportProcess(Long orderId, LocalDate reportDate) {
        OrderInfo.OrderItems orderItems = orderService.findOrderItemsByOrderId(orderId);

        // 상품별 수량 집계
        Map<Long, Long> productStats = orderItems.getOrderItemCountMap();

        //금일 상품 판매 집계 Key 등록 여부 확인(최초 등록 시 TTL 설정)
        boolean isExist = salesReportService.existByLocalDate(SalesReportCommand.ReportDate.of(reportDate));

        //Redis 저장
        productStats.forEach((productId, quantity) -> {
            salesReportService.increaseDailySalesReport(SalesReportCommand.IncreaseSalesReport.of(reportDate, productId, quantity.doubleValue()));
        });

        setSalesReportKeyTtlIfNotExist(isExist, reportDate, Duration.ofHours(25));
    }

    public static String getDailySalesReportKey(LocalDate date){
        return RedisKeys.DAILY_SALES_REPORT.format(date);
    }

    private void setSalesReportKeyTtlIfNotExist(boolean isExist, LocalDate reportDate, Duration duration) {
        if(!isExist){
            salesReportService.setDailySalesReportKeyTTL(SalesReportCommand.SalesReportTTL.of(reportDate, duration));
        }
    }


}
