package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderInfo;
import lombok.Getter;

public class OrderResult {

    @Getter
    public static class Order {

        Long orderId;
        Integer totalPrice;

        public static Order of(Long orderId, Integer totalPrice) {
            return new Order(orderId, totalPrice);
        }

        public static Order from(OrderInfo.Create create) {
            return new Order(create.getOrderId(), create.getTotalPrice());
        }

        private Order(Long orderId, Integer totalPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
        }

    }

}
