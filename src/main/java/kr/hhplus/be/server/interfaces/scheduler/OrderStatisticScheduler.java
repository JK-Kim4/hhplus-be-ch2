package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatistics;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsCommand;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrderStatisticScheduler {

    private final OrderService orderService;
    private final OrderStatisticsService orderStatisticsService;

    public OrderStatisticScheduler(
            OrderService orderService,
            OrderStatisticsService  orderStatisticsService) {
        this.orderService = orderService;
        this.orderStatisticsService = orderStatisticsService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void orderStatistic() {
        LocalDate targetDate = LocalDate.now().minusDays(1);

        List<Order> orders = orderService.findOrdersByDateAndStatus(targetDate, OrderStatus.PAYMENT_COMPLETED);

        List<OrderItem> orderItems = orderService.findOrderItemsByOrderIds(orders);

        List<OrderStatistics> orderStatistics = OrderStatistics.calculateOrderStatistics(orderItems, targetDate);

        orderStatisticsService.save(new OrderStatisticsCommand(orderStatistics));

    }


}
