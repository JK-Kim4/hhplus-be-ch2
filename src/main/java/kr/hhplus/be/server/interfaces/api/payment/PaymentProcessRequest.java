package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentProcessRequest {

    @Schema(name = "orderId", description = "주문 고유번호", example = "10")
    private Long orderId;

    @Schema(name = "userId", description = "사용자 고유번호", example = "8")
    private Long userId;

    @Schema(name = "finalPaymentPrice", description = "최종 결제 금액", example = "9000")
    private Integer finalPaymentPrice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getFinalPaymentPrice() {
        return finalPaymentPrice;
    }

    public void setFinalPaymentPrice(Integer finalPaymentPrice) {
        this.finalPaymentPrice = finalPaymentPrice;
    }
}


