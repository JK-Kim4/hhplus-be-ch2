package kr.hhplus.be.server.domain.order;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {

    @Getter
    public static class Create{

        Long orderId;
        Integer totalPrice;

        public static Create from(Order save) {
            return new Create(save.getId(), save.getTotalPrice());
        }

        public Create(Long orderId, Integer totalPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
        }
    }

    @Getter
    public static class Payment {

        Long userId;
        Long orderId;
        Integer totalPrice;
        Integer finalPrice;
        List<OrderItem> orderItems = new ArrayList<>();

        public static Payment from(Order order) {
            Payment payment = new Payment(order.getOrderUserId(), order.getId(), order.getTotalPrice(), order.getFinalPaymentPrice());
            payment.setOrderItems(order.getOrderItems());

            return payment;
        }

        private void setOrderItems(List<kr.hhplus.be.server.domain.order.OrderItem> orderItems) {
            orderItems.forEach(orderItem -> this.orderItems.add(OrderItem.of(orderItem.getItemId(), orderItem.getQuantity())));
        }

        private Payment(Long userId, Long orderId, Integer totalPrice, Integer finalPrice) {
            this.userId = userId;
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.finalPrice = finalPrice;
        }
    }

    @Getter
    public static class OrderItem {

        Long itemId;
        Integer quantity;

        public static OrderItem of(Long itemId, Integer quantity) {
            return new OrderItem(itemId, quantity);
        }

        private OrderItem(Long itemId, Integer quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }
}
