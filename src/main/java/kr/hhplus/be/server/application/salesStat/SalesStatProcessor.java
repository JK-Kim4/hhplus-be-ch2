package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.salesStat.RedisSalesStatService;
import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Transactional
public class SalesStatProcessor {

    public static final String SALES_REPORT_KEY_PREFIX = "products:dailySalesReport:";
    public static final String SALES_REPORT_IGNORE_VALUE = "__created__";
    private final OrderService orderService;
    private final RedisSalesStatService redisSalesStatService;

    public SalesStatProcessor(
            OrderService orderService,
            RedisSalesStatService redisSalesStatService) {
        this.orderService = orderService;
        this.redisSalesStatService = redisSalesStatService;
    }

    public void dailySalesReportProcess(Long orderId, String key) {
        OrderInfo.OrderItems orderItems = orderService.findOrderItemsByOrderId(orderId);

        // 상품별 수량 집계
        Map<Long, Long> productStats = orderItems.getOrderItemCountMap();

        //금일 상품 판매 집계 Key 등록 여부 확인(최초 등록 시 TTL 설정)
        setSalesReportKeyTtlIfNotExist(key);

        //Redis 저장
        productStats.forEach((productId, quantity) -> {
            redisSalesStatService.incrementZSetScoreByKeyWithMember(SalesStatCommand.RedisAddSortedSet.of(key, productId.toString(), quantity.doubleValue()));
        });
    }

    private void setSalesReportKeyTtlIfNotExist(String key) {
        if(!redisSalesStatService.hasKey(key)){
            redisSalesStatService.incrementZSetScoreByKeyWithMember(SalesStatCommand.RedisAddSortedSet.of(key, "__created__", 0)); // key 생성
            redisSalesStatService.setExpireTtl(key, Duration.ofHours(25)); // TTL 설정
        }
    }

    public static String getDailySalesReportKey(LocalDate date){
        return SALES_REPORT_KEY_PREFIX + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
