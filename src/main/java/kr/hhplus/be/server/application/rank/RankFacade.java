package kr.hhplus.be.server.application.rank;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.rank.RankService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RankFacade {

    private final OrderService orderService;
    private final RankService rankService;

    public RankFacade(OrderService orderService, RankService rankService) {
        this.orderService = orderService;
        this.rankService = rankService;
    }

    public List<Rank> createRankList(LocalDate targetDate) {
        //주문 목록 조회
        List<Order> orders = orderService.findAllByOrderDate(targetDate);
        List<Long> orderIds = orders.stream()
                .filter(order -> order.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETED))
                .map(Order::getId).toList();

        //주문 내 주문 아이템 목록 조회
        List<OrderItem> orderItemList = orderService.findAllByOrderIds(orderIds);

        //랭크 리스트 생성
        List<Rank> rankList = Rank.calculate(orderItemList, targetDate);

        //저장
        rankService.saveAll(rankList);

        return rankList;
    }
}
