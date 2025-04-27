package kr.hhplus.be.server.domain.payment;

import java.time.LocalDateTime;

public class PaymentHistory {

    public Long id;
    public Long userId;
    public Long orderId;
    public Long paymentId;
    public Integer price;
    public LocalDateTime createdAt;

    public PaymentHistory() {}

    public PaymentHistory(Payment payment) {

        this.userId = payment.getOrder().getOrderUserId();
        this.orderId = payment.getOrder().getId();
        this.paymentId = payment.getId();
        this.price = payment.getPaymentPrice();
        this.createdAt = LocalDateTime.now();
    }
}
