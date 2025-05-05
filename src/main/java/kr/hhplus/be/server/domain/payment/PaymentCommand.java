package kr.hhplus.be.server.domain.payment;

import lombok.Getter;

public class PaymentCommand {

    @Getter
    public static class Create{
        Long orderId;
        Long userId;

        public static Create of(Long orderId, Long userId) {
            return new Create(orderId, userId);
        }

        private Create(Long orderId, Long userId) {
            this.orderId = orderId;
            this.userId = userId;
        }
    }
}
