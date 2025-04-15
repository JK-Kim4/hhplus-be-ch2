package kr.hhplus.be.server.application.orderPayment.result;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentResult {

    @Getter
    public static class Create {

        private Long paymentId;
        private String paymentStatus;
        private LocalDateTime createdAt;

        public Create(
                Long paymentId,
                PaymentStatus paymentStatus,
                LocalDateTime createdAt) {
            this.paymentId = paymentId;
            this.paymentStatus = paymentStatus.name();
            this.createdAt = createdAt;
        }

        public static Create from(Payment payment) {
            return new Create(payment.getId(), payment.getPaymentStatus(), payment.getCreatedAt());
        }


    }

    @Getter
    public static class Process {

        private Long paymentId;
        private Integer paymentPrice;
        private String paymentStatus;
        private LocalDateTime paymentRequestDateTime;
        private LocalDateTime paymentResponseDateTime;
        private LocalDateTime createdAt;

        public static Process from(Payment payment) {
            return new Process(
                payment.getId(), payment.getPaymentPrice(), payment.getPaymentStatus(),
                payment.getPaymentRequestDateTime(), payment.getPaymentResponseDateTime(), payment.getCreatedAt());
        }

        private Process(Long paymentId, Integer paymentPrice, PaymentStatus paymentStatus, LocalDateTime paymentRequestDateTime, LocalDateTime paymentResponseDateTime, LocalDateTime createdAt) {
            this.paymentId = paymentId;
            this.paymentPrice = paymentPrice;
            this.paymentStatus = paymentStatus.name();
            this.paymentRequestDateTime = paymentRequestDateTime;
            this.paymentResponseDateTime = paymentResponseDateTime;
            this.createdAt = createdAt;
        }
    }
}
