package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.PaymentInfo;
import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentResult {

    @Getter
    public static class Payment {

        Long paymentId;
        Integer paymentPrice;
        LocalDateTime paymentDateTime;

        public static Payment from(PaymentInfo.Create create) {
            return new Payment(create.getPaymentId(), create.getPaymentPrice(), create.getPaymentDateTime());
        }

        private Payment(Long paymentId, Integer paymentPrice, LocalDateTime paymentDateTime) {
            this.paymentId = paymentId;
            this.paymentPrice = paymentPrice;
            this.paymentDateTime = paymentDateTime;
        }
    }
}
