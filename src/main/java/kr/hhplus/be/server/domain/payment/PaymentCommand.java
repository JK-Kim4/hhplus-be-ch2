package kr.hhplus.be.server.domain.payment;

import lombok.Builder;
import lombok.Getter;

public class PaymentCommand {

    @Getter
    public static class Pay {
        Long orderId;
        Long userId;

        public static Pay of(Long userId, Long orderId) {
            return Pay.builder().orderId(orderId).userId(userId).build();
        }

        @Builder
        private Pay(Long orderId, Long userId) {
            this.orderId = orderId;
            this.userId = userId;
        }
    }
}
