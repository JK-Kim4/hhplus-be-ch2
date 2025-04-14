package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.command.OrderCommand;

import java.util.List;

public class OrderPaymentCriteria {

    private Long userId;
    private Long userCouponId;
    private List<OrderItemCriteria> orderItems;

    public OrderPaymentCriteria(Long userId, Long userCouponId, List<OrderItemCriteria> orderItems) {
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderItems = orderItems;

    }

    public Long getUserId() {
        return userId;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public List<OrderItemCriteria> getOrderItems() {
        return orderItems;
    }

    public OrderCommand.Create toCommand() {
        List<OrderCommand.OrderItem> orderItemList = orderItems.stream()
                .map(OrderItemCriteria::toCommand)
                .toList();

        return OrderCommand.Create.of(this.userId, this.userCouponId, orderItemList);
    }
}
