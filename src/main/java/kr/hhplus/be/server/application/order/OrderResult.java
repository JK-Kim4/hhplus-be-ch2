package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.command.OrderInfo;

public class OrderResult {


    public static class Create {

        private Long orderId;
        private Integer totalPrice;

        public static Create from(OrderInfo.Create info) {
            return new Create(info.getOrderId(), info.getTotalPrice());
        }

        public Create(Long orderId, Integer totalPrice) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
        }
    }
}
