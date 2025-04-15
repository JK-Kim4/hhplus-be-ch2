package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderUser;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    private Long id;
    private OrderUser orderUser;
    private Integer paymentPrice;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentRequestDateTime;
    private LocalDateTime paymentResponseDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment(Order order, User user){

        if(!user.getId().equals(order.getUserId())){
            throw new IllegalArgumentException("주문자 정보가 일치하지않습니다.");
        }

        this.orderUser = new OrderUser(order, user);
        this.paymentPrice = order.getFinalPaymentPrice();
        this.paymentStatus = PaymentStatus.PAYMENT_PENDING;
        this.orderUser.updateOrderStatus(OrderStatus.PAYMENT_WAITING);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order getOrder() {
        return this.orderUser.getOrder();
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

    public boolean isPayable(){
        if(this.paymentStatus != PaymentStatus.PAYMENT_PENDING){
            throw new IllegalArgumentException("진행할 수 없는 결제입니다.");
        }

        return orderUser.hasEnoughPoint();
    }

    public void pay(){
        this.orderUser.deductOrderItemStock();
        this.logRequestDateTime(LocalDateTime.now());
        this.orderUser.deductPrice(paymentPrice);
        this.paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
        this.logResponseDateTime(LocalDateTime.now());
        this.orderUser.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
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
