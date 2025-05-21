package kr.hhplus.be.server.domain.payment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PaymentInfo {

    @Getter
    public static class Pay {

        Long paymentId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Pay from(kr.hhplus.be.server.domain.payment.Payment payment) {
            return new Pay(payment.getId(), payment.getFinalPrice(), payment.getPaidAt());
        }

        @Builder
        private Pay(Long paymentId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }
    }

    @Getter
    public static class Create {
        Long paymentId;
        Long orderId;

        public static Create of(Long paymentId, Long orderId) {
            return Create.builder().paymentId(paymentId).orderId(orderId).build();
        }

        public static Create from(kr.hhplus.be.server.domain.payment.Payment payment) {
            return Create.builder().paymentId(payment.getId()).orderId(payment.getOrderId()).build();
        }

        @Builder
        private Create(Long paymentId, Long orderId) {
            this.paymentId = paymentId;
            this.orderId = orderId;
        }
    }

    @Getter @EqualsAndHashCode
    public static class Complete {

        Long paymentId;
        Long orderId;
        BigDecimal paidAmount;
        LocalDateTime paidAt;

        public static Complete from(kr.hhplus.be.server.domain.payment.Payment payment) {
            return Complete.builder()
                        .paymentId(payment.getId())
                        .orderId(payment.getOrderId())
                        .paidAmount(payment.getFinalPrice())
                        .paidAt(payment.getPaidAt())
                    .build();
        }

        @Builder
        private Complete(Long paymentId, Long orderId, BigDecimal paidAmount, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.paidAmount = paidAmount;
            this.paidAt = paidAt;
        }


    }

    @Getter
    public static class Payments {
        List<PaymentInfo.Payment> payments;

        public Set<Long> getOrderIds(){
            return payments.stream()
                    .map(PaymentInfo.Payment::getOrderId)
                    .collect(Collectors.toSet());
        }

        public static Payments from(List<kr.hhplus.be.server.domain.payment.Payment> payments) {
            return Payments.builder()
                    .payments(
                            payments.stream()
                                    .map(PaymentInfo.Payment::from)
                                    .toList()
                    )
                    .build();
        }

        @Builder
        private Payments(List<PaymentInfo.Payment> payments) {
            this.payments = payments;
        }
    }

    @Getter
    public static class Payment {
        private Long paymentId;
        private Long orderId;
        private Long userId;
        private BigDecimal finalPrice;
        private LocalDateTime paidAt;

        public static PaymentInfo.Payment from(kr.hhplus.be.server.domain.payment.Payment payment){
            return Payment.builder()
                    .paymentId(payment.getId())
                    .orderId(payment.getOrderId())
                    .userId(payment.getUserId())
                    .finalPrice(payment.getFinalPrice())
                    .paidAt(payment.getPaidAt())
                    .build();
        }

        @Builder
        private Payment(Long paymentId, Long orderId, Long userId, BigDecimal finalPrice, LocalDateTime paidAt) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.userId = userId;
            this.finalPrice = finalPrice;
            this.paidAt = paidAt;
        }

    }
}
