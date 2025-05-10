package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class OrderFacade {

    private final OrderService orderService;

    public OrderFacade(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResult.Create order(OrderCriteria.Create criteria){
        orderService.validationUserOrder(criteria.getUserId());

        orderService.validationOrderItems(OrderCriteria.Items.toCommandList(criteria.getItems()));

        OrderInfo.Create order = orderService.create(OrderCriteria.Create.toCommand(criteria));

        Optional.ofNullable(criteria.getUserCouponId())
                .ifPresent(userCouponId ->
                        orderService.applyCoupon(OrderCommand.ApplyCoupon.of(userCouponId, order.getOrderId())));

        return OrderResult.Create.from(order);
    }
}
