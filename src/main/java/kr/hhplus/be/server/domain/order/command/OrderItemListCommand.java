package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.orderItem.OrderItem;

import java.util.List;

public class OrderItemListCommand {

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public static class Response {

        private List<OrderItem> orderItems;

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public Response(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }
}
