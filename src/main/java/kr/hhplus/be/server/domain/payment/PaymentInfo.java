package kr.hhplus.be.server.domain.payment;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentInfo {

    @Getter
    public static class Pay {

        Long paymentId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Pay from(Payment payment) {
            return new Pay(payment.getId(), payment.getFinalPrice(), payment.getPaidAt());
        }

        @Builder
        private Pay(Long paymentId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }
    }

    @Getter
    public static class Create {
        Long paymentId;
        Long orderId;

        public static Create of(Long paymentId, Long orderId) {
            return Create.builder().paymentId(paymentId).orderId(orderId).build();
        }

        public static Create from(Payment payment) {
            return Create.builder().paymentId(payment.getId()).orderId(payment.getOrderId()).build();
        }

        @Builder
        private Create(Long paymentId, Long orderId) {
            this.paymentId = paymentId;
            this.orderId = orderId;
        }
    }

    @Getter
    public static class Complete {

        Long paymentId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Complete from(Payment payment) {
            return Complete.builder().paymentId(payment.getId()).paidAmount(payment.getFinalPrice()).paidAt(payment.getPaidAt()).build();
        }

        @Builder
        private Complete(Long paymentId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }


    }
}
