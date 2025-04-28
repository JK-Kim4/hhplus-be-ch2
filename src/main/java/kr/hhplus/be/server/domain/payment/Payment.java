package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "pamynet_price")
    private Integer paymentPrice;

    @Column
    private LocalDateTime paymentRequestDateTime;

    @Column
    private LocalDateTime paymentResponseDateTime;

    public Payment(Order order, User user){
        validation(order, user);

        this.order = order;
        this.user = user;
        this.paymentPrice = order.getFinalPaymentPrice();
    }

    public Payment(Order order){
        validation(order);

        this.order = order;
        this.user = order.getUser();
        this.paymentPrice = order.getFinalPaymentPrice();
        order.registerPayment(this);

    }

    public static Payment createWithOrderValidation(Order order, User user) {
        if(!order.getUser().equals(user)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지않습니다.");
        }

        return new Payment(order);
    }

    public void updatePaymentResponseDateTime(LocalDateTime responseDateTime) {
        this.paymentResponseDateTime = responseDateTime;
    }

    public void isPayable(){
        if(user.point() < paymentPrice){
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }

    private void validation(Order order, User user) {
        if(Objects.isNull(order) || Objects.isNull(user)) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        if(!order.getUser().equals(user)){
            throw new IllegalArgumentException("결제 요청 사용자와 주문자 정보가 일치하지않습니다.");
        }
    }

    private void validation(Order order) {
        if(Objects.isNull(order)) {
            throw new IllegalArgumentException("주문 정보가 누락되었습니다.");
        }
    }

    private void authentication(User user){
        if(!user.equals(this.user)){
            throw new IllegalArgumentException("사용자 정보가 일치하지않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id)
                && Objects.equals(paymentPrice, payment.paymentPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentPrice);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentPrice=" + paymentPrice +
                '}';
    }
}
