package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.application.orderPayment.result.PaymentResult;

import java.time.LocalDateTime;

public class PaymentResponse {

    static public class Create{

        public Long paymentId;

        public LocalDateTime createdAt;

        public Create(Long paymentId, LocalDateTime createdAt) {
            this.paymentId = paymentId;
            this.createdAt = createdAt;
        }

        public static Create from(PaymentResult.Create payment) {
            return new Create(
                    payment.getPaymentId(),
                    payment.getCreatedAt());
        }
    }

    static public class Process {

        private Long paymentId;
        private String paymentResponseMessage;
        private LocalDateTime paymentRequestDateTime;
        private LocalDateTime paymentResponseDateTime;

        public static Process from(PaymentResult.Process process) {
            return new Process(
                    process.getPaymentId(),
                    process.getPaymentStatus(),
                    process.getPaymentRequestDateTime(),
                    process.getPaymentResponseDateTime());
        }

        public Process(Long paymentId, String paymentResponseMessage, LocalDateTime paymentRequestDateTime, LocalDateTime paymentResponseDateTime) {
            this.paymentId = paymentId;
            this.paymentResponseMessage = paymentResponseMessage;
            this.paymentRequestDateTime = paymentRequestDateTime;
            this.paymentResponseDateTime = paymentResponseDateTime;
        }
    }
}
