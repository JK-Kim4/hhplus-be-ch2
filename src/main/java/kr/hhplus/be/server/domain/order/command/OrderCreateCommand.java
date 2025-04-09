package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

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


    public static class Response {

        private Long orderId;
        private Long userId;
        private Integer totalPrice;
        private OrderStatus orderStatus;
        private LocalDateTime createdAt;

        public Long getOrderId() {
            return orderId;
        }

        public Long getUserId() {
            return userId;
        }

        public Integer getTotalPrice() {
            return totalPrice;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public Response(Order order) {
            this.orderId = order.getId();
            this.userId = order.getOrderUser().id();
            this.totalPrice = order.getTotalPrice();
            this.orderStatus = order.getOrderStatus();
            this.createdAt = order.getCreatedAt();
        }
    }

}
