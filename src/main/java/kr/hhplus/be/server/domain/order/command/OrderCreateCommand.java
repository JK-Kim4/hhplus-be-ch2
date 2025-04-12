package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.application.order.OrderPaymentCriteria;
import kr.hhplus.be.server.domain.order.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateCommand {

    private Long userId;
    private Long userCouponId;
    private List<OrderItemCreateCommand> orderItems;

    public Long getUserId() {
        return userId;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public List<OrderItemCreateCommand> getOrderItems() {
        return orderItems;
    }

    public OrderCreateCommand(Long userId, Long userCouponId, List<OrderItemCreateCommand> orderItems) {
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderItems = orderItems;
    }

    public static OrderCreateCommand from(OrderPaymentCriteria criteria){
        List<OrderItemCreateCommand> itemList =
                criteria.getOrderItems().stream()
                        .map(OrderItemCreateCommand::from)
                        .collect(Collectors.toList());

        return new OrderCreateCommand(
                criteria.getUserId(),
                criteria.getUserId(),
                itemList);
    }

    public static class Response {

        private Order order;

        public Response(Order order) {
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }
    }

}
