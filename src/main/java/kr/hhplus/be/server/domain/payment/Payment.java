package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

    private static Long cursor = 1L;

    private Long id;
    private Order order;
    private Integer paymentPrice;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment(){
        this.id = cursor++;
    }

    public Payment(Order order, Integer paymentPrice, PaymentStatus paymentStatus) {
        this.id = cursor++;
        this.order = order;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
    }

    public Payment(Order order){
        this.order = order;
        this.paymentPrice = order.getTotalPrice();
        this.paymentStatus = PaymentStatus.PAYMENT_PENDING;
        this.order.updateOrderStatus(OrderStatus.PAYMENT_WAITING);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void pay(){
        this.order.getOrderUser().deductPoint(this.paymentPrice);
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(order, payment.order) && Objects.equals(paymentPrice, payment.paymentPrice) && paymentStatus == payment.paymentStatus && Objects.equals(createdAt, payment.createdAt) && Objects.equals(updatedAt, payment.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, paymentPrice, paymentStatus, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", order=" + order +
                ", paymentPrice=" + paymentPrice +
                ", paymentStatus=" + paymentStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
