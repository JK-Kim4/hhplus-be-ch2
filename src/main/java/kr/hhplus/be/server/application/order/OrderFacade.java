package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.user.UserQueryService;
import kr.hhplus.be.server.domain.item.ItemInfo;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class OrderFacade {

    private final OrderService orderService;
    private final ItemService itemService;
    private final UserQueryService userQueryService;

    public OrderFacade(
            OrderService orderService,
            ItemService itemService,
            UserQueryService userQueryService) {
        this.orderService = orderService;
        this.itemService = itemService;
        this.userQueryService = userQueryService;
    }


    public OrderResult.Order order(OrderCriteria.Create criteria){
        //사용자 정보 검증
        userQueryService.findById(criteria.getUserId());

        //상품 정보 검증
        List<ItemInfo.OrderItem> orderItems = itemService.getOrderItemsV2(criteria.toOrderItemCommand());

        //주문 생성 요청(service)
        OrderInfo.Create order = orderService.createV2(criteria.toOrderCommand(orderItems));

        //결과 응답
        return OrderResult.Order.from(order);
    }
}
