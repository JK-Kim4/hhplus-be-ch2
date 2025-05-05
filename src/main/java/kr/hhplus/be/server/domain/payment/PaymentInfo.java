package kr.hhplus.be.server.domain.payment;

import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentInfo {

    @Getter
    public static class Create{

        Long paymentId;
        Integer paymentPrice;
        LocalDateTime paymentDateTime;

        public static Create from(Payment payment){
            return new Create(payment.getId(), payment.getPaymentPrice(), payment.getPaymentResponseDateTime());
        }

        private Create(Long paymentId, Integer paymentPrice, LocalDateTime paymentDateTime) {
            this.paymentId = paymentId;
            this.paymentPrice = paymentPrice;
            this.paymentDateTime = paymentDateTime;
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
