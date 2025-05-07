package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "payment", indexes = {
        @Index(name = "idx_join_user_id", columnList = "user_id"),
        @Index(name = "idx_join_order_id", columnList = "order_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "final_price"))
    private Price finalPrice;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public static Payment create(Long orderId, Long userId, Price finalPrice, LocalDateTime paidAt) {
        return new Payment(orderId, userId, finalPrice, paidAt);
    }

    public static Payment create(Order order) {
        return new Payment(order.getId(), order.getUserId(), Price.of(order.getFinalAmount()), LocalDateTime.now());
    }

    private Payment(Long orderId, Long userId, Price finalPrice, LocalDateTime paidAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.finalPrice = finalPrice;
        this.paidAt = paidAt;
    }

    public BigDecimal getFinalPrice() {
        return this.finalPrice.getAmount();
    }

    public void pay(Balance balance) {
        balance.deduct(this.finalPrice.getAmount());
    }

    public void complete(Order order) {
        order.completePayment();
    }
}
