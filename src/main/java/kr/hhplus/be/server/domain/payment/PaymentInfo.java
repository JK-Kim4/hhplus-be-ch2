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
}
