package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.interfaces.api.payment.PaymentRequest;
import lombok.Getter;

public class PaymentCriteria {

    @Getter
    public static class Create {
        Long userId;
        Long orderId;

        public static Create of(Long userId, Long orderId) {
            return new Create(userId, orderId);
        }

        public static Create from(PaymentRequest.Payment payment){
            return new Create(payment.getUserId(), payment.getOrderId());
        }

        private Create(Long userId, Long orderId) {
            this.userId = userId;
            this.orderId = orderId;
        }
    }
}
