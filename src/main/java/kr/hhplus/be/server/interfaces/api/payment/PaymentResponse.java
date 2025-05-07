package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.application.payment.PaymentResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    @Getter
    public static class Pay {

        Long paymentId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Pay from(PaymentResult.Pay pay) {
            return Pay.builder()
                        .paymentId(pay.getPaymentId())
                        .paidAmount(pay.getPaidAmount())
                        .paidAt(pay.getPaidAt())
                    .build();
        }

        @Builder
        private Pay(Long paymentId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }
    }
}
