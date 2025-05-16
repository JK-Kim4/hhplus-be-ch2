package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.salesStat.RedisSalesStatService;
import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import kr.hhplus.be.server.domain.salesStat.SalesStatInfo;
import kr.hhplus.be.server.domain.salesStat.SalesStatService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SalesStatFacade {

    private final SalesStatService salesStatService;
    private final RedisSalesStatService redisSalesStatService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public SalesStatFacade(
            SalesStatService salesStatService, RedisSalesStatService redisSalesStatService,
            OrderService orderService, PaymentService paymentService) {
        this.salesStatService = salesStatService;
        this.redisSalesStatService = redisSalesStatService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    public SalesStatResult.SalesStats getSalesStatsByDate(SalesStatCriteria.TargetDate criteria){
        PaymentInfo.Payments payments = paymentService.findAllByPaidDate(criteria.getTargetDate());
        Set<Long> orderIds = payments.getOrderIds();

        OrderInfo.OrderItems orderItems = orderService.findOrderItemsByOrderIds(orderIds);
        Map<Long, Long> orderItemCountMap = orderItems.getOrderItemCountMap();

        return SalesStatResult.SalesStats.of(orderItemCountMap, criteria.targetDate);
    }

    public void persistRedisSalesStatToDb(){
        LocalDate targetDate = LocalDate.now().minusDays(1);
        String targetKey = SalesStatProcessor.getDailySalesReportKey(targetDate);

        SalesStatInfo.RedisTypedScoreSet typedScoreSet = redisSalesStatService.findReverseRangeWithScoresSetByKey(targetKey);

        List<SalesStatInfo.SalesReport> list = salesStatService.getSalesReportsFromRedisTypedScoreSet(typedScoreSet,targetDate);

        salesStatService.createAll(SalesStatCommand.Creates.fromList(list));
    }


}
