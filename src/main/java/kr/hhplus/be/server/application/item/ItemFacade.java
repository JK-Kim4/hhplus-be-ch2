package kr.hhplus.be.server.application.item;

import kr.hhplus.be.server.domain.item.ItemCommand;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.rank.RankService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class ItemFacade {

    private final OrderService orderService;
    private final RankService rankService;
    private final ItemService itemService;

    public ItemFacade(OrderService orderService, RankService rankService, ItemService itemService) {
        this.orderService = orderService;
        this.rankService = rankService;
        this.itemService = itemService;
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
        return Rank.calculate(orderItemList, targetDate);
    }

    public void saveRankList(List<Rank> rankList) {
        rankService.saveAll(rankList);
    }

    public ItemCommand.Item findItemById(Long id) {
        return itemService.findById(id);
    }
}
