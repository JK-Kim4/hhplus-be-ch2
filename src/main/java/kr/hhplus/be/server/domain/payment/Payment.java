package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderUser;
import kr.hhplus.be.server.domain.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

    private Long id;
    private OrderUser orderUser;
    private Integer paymentPrice;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentRequestDateTime;
    private LocalDateTime paymentResponseDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment(){
    }

    public Payment(Order order, User user, Integer paymentPrice, PaymentStatus paymentStatus) {
        this.orderUser = new OrderUser(order, user);
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Payment(Order order, User user){
        this.orderUser = new OrderUser(order, user);
        this.paymentPrice = order.getFinalPaymentPrice();
        this.paymentStatus = PaymentStatus.PAYMENT_PENDING;
        this.orderUser.updateOrderStatus(OrderStatus.PAYMENT_WAITING);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return orderUser.getOrder();
    }

    public User getUser() {
        return orderUser.getUser();
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentRequestDateTime() {
        return paymentRequestDateTime;
    }

    public LocalDateTime getPaymentResponseDateTime() {
        return paymentResponseDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void logRequestDateTime(LocalDateTime requestDateTime) {
        this.paymentRequestDateTime = requestDateTime;
    }

    public void logResponseDateTime(LocalDateTime responseDateTime) {
        this.paymentResponseDateTime = responseDateTime;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void isPayable(){
        if(this.paymentStatus != PaymentStatus.PAYMENT_PENDING){
            throw new IllegalArgumentException("진행할 수 없는 결제입니다.");
        }

        orderUser.hasEnoughPoint();
    }

    public void pay(){
        isPayable();
        this.logRequestDateTime(LocalDateTime.now());
        this.orderUser.deductPrice(paymentPrice);
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
        this.logResponseDateTime(LocalDateTime.now());
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id)
                && Objects.equals(orderUser, payment.orderUser)
                && Objects.equals(paymentPrice, payment.paymentPrice)
                && paymentStatus == payment.paymentStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderUser, paymentPrice, paymentStatus);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentPrice=" + paymentPrice +
                ", paymentStatus=" + paymentStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
