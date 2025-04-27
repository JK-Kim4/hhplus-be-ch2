package kr.hhplus.be.server.interfaces.common.scheduler;

import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.rank.RankService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderStatisticScheduler {

    private final OrderService orderService;
    private final ItemService itemService;
    private final RankService rankService;

    public OrderStatisticScheduler(
            OrderService orderService,
            ItemService itemService,
            RankService  rankService) {
        this.orderService = orderService;
        this.itemService = itemService;
        this.rankService = rankService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void orderStatistic() {
        LocalDate targetDate = LocalDate.now().minusDays(1);

        List<Order> orders = orderService.findOrdersByDateAndStatus(targetDate, OrderStatus.PAYMENT_COMPLETED);

        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        List<OrderItem> orderItems = orderService.findAllByOrderIds(orderIds);

        List<Rank> orderStatistics = Rank.calculateOrderStatistics(orderItems, targetDate);

        rankService.saveAll(orderStatistics);

    }


}
