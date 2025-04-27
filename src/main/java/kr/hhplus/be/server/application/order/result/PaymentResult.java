package kr.hhplus.be.server.application.order.result;

import kr.hhplus.be.server.domain.payment.Payment;
import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentResult {

    @Getter
    public static class Create {

        private Long paymentId;
        private LocalDateTime createdAt;

        public Create(
                Long paymentId,
                LocalDateTime createdAt) {
            this.paymentId = paymentId;
            this.createdAt = createdAt;
        }

        public static Create from(Payment payment) {
            return new Create(payment.getId(), payment.getCreatedAt());
        }


    }

    @Getter
    public static class Process {

        private Long paymentId;
        private Integer paymentPrice;
        private LocalDateTime paymentRequestDateTime;
        private LocalDateTime paymentResponseDateTime;
        private LocalDateTime createdAt;

        public static Process from(Payment payment) {
            return new Process(
                payment.getId(), payment.getPaymentPrice(),
                    payment.getPaymentRequestDateTime(), payment.getPaymentResponseDateTime(), payment.getCreatedAt());
        }

        private Process(Long paymentId, Integer paymentPrice, LocalDateTime paymentRequestDateTime, LocalDateTime paymentResponseDateTime, LocalDateTime createdAt) {
            this.paymentId = paymentId;
            this.paymentPrice = paymentPrice;
            this.paymentRequestDateTime = paymentRequestDateTime;
            this.paymentResponseDateTime = paymentResponseDateTime;
            this.createdAt = createdAt;
        }
    }
}
