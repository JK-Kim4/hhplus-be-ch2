package kr.hhplus.be.server.interfaces.api.payment;

import lombok.Getter;

public class PaymentRequest {

    @Getter
    public static class Payment {

        Long userId;
        Long orderId;

        public Payment(Long userId, Long orderId) {
            this.userId = userId;
            this.orderId = orderId;
        }
    }
}
