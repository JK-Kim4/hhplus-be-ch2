package kr.hhplus.be.server.domain.payment;

import lombok.Builder;
import lombok.Getter;

public class PaymentCommand {

    @Getter
    public static class Pay {
        Long userId;
        Long paymentId;

        public static Pay of(Long userId, Long paymentId) {
            return Pay.builder().paymentId(paymentId).userId(userId).build();
        }

        @Builder
        private Pay(Long paymentId, Long userId) {
            this.paymentId = paymentId;
            this.userId = userId;
        }
    }

    @Getter
    public static class Create {
        Long orderId;

        public static Create of(Long orderId) {
            return Create.builder().orderId(orderId).build();
        }

        @Builder
        private Create(Long orderId) {
            this.orderId = orderId;
        }
    }

    @Getter
    public static class Complete {
        Long orderId;
        Long paymentId;

        public static Complete of(Long orderId, Long paymentId) {
            return Complete.builder().orderId(orderId).paymentId(paymentId).build();
        }

        @Builder
        private Complete(Long orderId, Long paymentId) {
            this.orderId = orderId;
            this.paymentId = paymentId;
        }
    }
}
