package kr.hhplus.be.server.domain.payment.command;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;

public class PaymentProcessCommand {

    private Long paymentId;

    public Long getPaymentId() {
        return paymentId;
    }

    public PaymentProcessCommand(Long paymentId) {
        this.paymentId = paymentId;
    }

    public static class Response {
        private PaymentHistory paymentHistory;
        private Payment payment;

        public PaymentHistory getPaymentHistory() {
            return paymentHistory;
        }

        public Payment getPayment() {
            return payment;
        }

        public Response(PaymentHistory paymentHistory) {
            this.paymentHistory = paymentHistory;
        }

        public Response(Payment payment) {
            this.payment = payment;
        }
    }

}
