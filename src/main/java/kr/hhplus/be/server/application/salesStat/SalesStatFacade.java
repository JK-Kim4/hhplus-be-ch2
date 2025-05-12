package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class SalesStatFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public SalesStatFacade(OrderService orderService, PaymentService paymentService) {
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
}
