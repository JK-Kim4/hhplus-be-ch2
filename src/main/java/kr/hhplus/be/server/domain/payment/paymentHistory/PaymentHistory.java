package kr.hhplus.be.server.domain.payment.paymentHistory;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentHistory {

    public Long id;
    public Long userId;
    public Long orderId;
    public Long paymentId;
    public Integer price;
    public PaymentStatus status;
    public LocalDateTime createdAt;

    public PaymentHistory() {}

    public PaymentHistory(Payment payment) {

        this.userId = payment.getOrder().getUserId();
        this.orderId = payment.getOrder().getId();
        this.paymentId = payment.getId();
        this.price = payment.getPaymentPrice();
        this.status = payment.getPaymentStatus();
        this.createdAt = LocalDateTime.now();
    }
}
