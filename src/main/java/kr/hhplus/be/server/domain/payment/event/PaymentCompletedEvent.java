package kr.hhplus.be.server.domain.payment.event;

import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.product.Price;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCompletedEvent implements Serializable {

    private Long paymentId;
    private Long orderId;
    private Price paymentPrice;
    private LocalDateTime paidAt;

    public static PaymentCompletedEvent from(PaymentInfo.Complete info){
        return PaymentCompletedEvent.builder()
                .paymentId(info.getPaymentId())
                .orderId(info.getOrderId())
                .paymentPrice(Price.of(info.getPaidAmount()))
                .paidAt(info.getPaidAt())
                .build();
    }

    public PaymentInfo.Complete toInfo(){
        return PaymentInfo.Complete.builder()
                .paymentId(this.paymentId)
                .orderId(this.orderId)
                .paidAmount(this.paymentPrice.getAmount())
                .paidAt(this.paidAt)
                .build();
    }


    @Builder
    private PaymentCompletedEvent(Long paymentId, Long orderId, Price paymentPrice, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentPrice = paymentPrice;
        this.paidAt = paidAt;
    }
}
