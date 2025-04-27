package kr.hhplus.be.server.application.order.result;

import lombok.Getter;

@Getter
public class OrderPaymentResult {

    private Long orderId;
    private Long paymentId;
    private Integer finalPaymentPrice;

    public OrderPaymentResult(Long orderId, Long paymentId, Integer finalPaymentPrice) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.finalPaymentPrice = finalPaymentPrice;
    }

}
