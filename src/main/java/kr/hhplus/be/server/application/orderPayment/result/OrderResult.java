package kr.hhplus.be.server.application.orderPayment.result;

import kr.hhplus.be.server.domain.order.Order;
import lombok.Getter;

public class OrderResult {


    @Getter
    public static class Create {

        private Long orderId;
        private Integer totalPrice;
        private Integer payPrice;

        public static Create from(Order order) {
            return new Create(order.getId(), order.getTotalPrice(), order.getFinalPaymentPrice());
        }

        public Create(Long orderId, Integer totalPrice, Integer payPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.payPrice = payPrice;
        }

        @Override
        public String toString() {
            return "Create{" +
                    "orderId=" + orderId +
                    ", totalPrice=" + totalPrice +
                    ", payPrice=" + payPrice +
                    '}';
        }
    }
}
