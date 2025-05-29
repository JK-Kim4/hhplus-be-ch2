package kr.hhplus.be.server.interfaces.payment.api;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import lombok.Builder;
import lombok.Getter;

public class PaymentRequest {

    @Getter
    public static class Pay {
        Long orderId;
        Long userId;

        public static Pay of(Long orderId, Long userId) {
            return Pay.builder().orderId(orderId).userId(userId).build();
        }

        public PaymentCriteria.Pay toCriteria() {
            return PaymentCriteria.Pay.of(this.orderId, this.userId);
        }

        @Builder
        private Pay(Long orderId, Long userId) {
            this.orderId = orderId;
            this.userId = userId;
        }
    }
}
