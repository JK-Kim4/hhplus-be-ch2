package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResult {

    @Getter
    public static class Pay {

        Long paymentId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Pay from(PaymentInfo.Complete payment) {
            return new Pay(payment.getPaymentId(), payment.getPaidAmount(), payment.getPaidAt());
        }

        @Builder
        private Pay(Long paymentId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }
    }
}
