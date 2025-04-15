package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;

public class PaymentInfo {

    public static class Create{

        private Long paymentId;

        public Create(Long paymentId) {
            this.paymentId = paymentId;
        }

        public Long getPaymentId() {
            return paymentId;
        }

    }


    public static class Process{

        private PaymentHistory paymentHistory;
        private Payment payment;

        public PaymentHistory getPaymentHistory() {
            return paymentHistory;
        }

        public Payment getPayment() {
            return payment;
        }

        public Process(PaymentHistory paymentHistory) {
            this.paymentHistory = paymentHistory;
        }

        public Process(Payment payment) {
            this.payment = payment;
        }

    }
}
