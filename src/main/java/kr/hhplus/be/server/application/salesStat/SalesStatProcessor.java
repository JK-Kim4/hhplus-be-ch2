package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.common.redis.RedisKeys;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportCommand;
import kr.hhplus.be.server.domain.salesStat.salesReport.SalesReportService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Component
@Transactional
public class SalesStatProcessor {

    private final OrderService orderService;
    private final SalesReportService salesReportService;

    public SalesStatProcessor(
            OrderService orderService,
            SalesReportService salesReportService) {
        this.orderService = orderService;
        this.salesReportService = salesReportService;
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
