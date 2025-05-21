package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.product.Price;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCompleteEvent {

    private Long paymentId;
    private Long orderId;
    private Price paymentPrice;
    private LocalDateTime paidAt;

    public static PaymentCompleteEvent from(PaymentInfo.Complete info){
        return PaymentCompleteEvent.builder()
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
    private PaymentCompleteEvent(Long paymentId, Long orderId, Price paymentPrice, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentPrice = paymentPrice;
        this.paidAt = paidAt;
    }
}
