package kr.hhplus.be.server.common.event;

import kr.hhplus.be.server.domain.payment.PaymentInfo;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCompletedEvent implements Serializable {

    private Long paymentId;
    private Long orderId;
    private BigDecimal paymentPrice;
    private LocalDateTime paidAt;

    public static PaymentCompletedEvent of(Long paymentId, Long orderId, BigDecimal paymentPrice, LocalDateTime paidAt){
        return PaymentCompletedEvent.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .paymentPrice(paymentPrice)
                .paidAt(paidAt)
                .build();
    }

    public static PaymentCompletedEvent from(PaymentInfo.Complete info){
        return PaymentCompletedEvent.builder()
                .paymentId(info.getPaymentId())
                .orderId(info.getOrderId())
                .paymentPrice(info.getPaidAmount())
                .paidAt(info.getPaidAt())
                .build();
    }


    @Builder
    private PaymentCompletedEvent(Long paymentId, Long orderId, BigDecimal paymentPrice, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentPrice = paymentPrice;
        this.paidAt = paidAt;
    }
}
