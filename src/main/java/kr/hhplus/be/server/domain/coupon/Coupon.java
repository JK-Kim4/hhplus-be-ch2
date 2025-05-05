package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private int quantity; // 발급 가능한 총 수량

    @Enumerated(EnumType.STRING)
    private DiscountPolicy discountPolicy;

    @Column(nullable = false)
    private BigDecimal discountAmount; // 정액이면 금액, 정률이면 비율(%)

    private LocalDate expireDate;

    protected Coupon(
            String name, int quantity, DiscountPolicy discountPolicy,
            BigDecimal discountAmount, LocalDate expireDate) {
        this.name = name;
        this.quantity = quantity;
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
        this.expireDate = expireDate;
    }

    public static Coupon create(
            String name, int quantity, DiscountPolicy discountPolicy,
            BigDecimal discountAmount, LocalDate expireDate) {
        return new Coupon(name, quantity, discountPolicy, discountAmount, expireDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expireDate);
    }

    public void decreaseQuantity() {
        if (quantity <= 0) throw new IllegalStateException("쿠폰 수량이 부족합니다.");
        quantity--;
    }

    public BigDecimal calculateDiscount(BigDecimal orderTotalAmount) {
        return discountPolicy.calculate(orderTotalAmount, discountAmount);
    }
}
