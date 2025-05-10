package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "user_coupon", indexes = {
        @Index(name = "idx_join_coupon_id", columnList = "coupon_id"),
        @Index(name = "idx_join_user_id", columnList = "user_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Coupon coupon;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    protected UserCoupon(Coupon coupon, Long userId, LocalDateTime issuedAt) {
        this.coupon = coupon;
        this.userId = userId;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon issue(Coupon coupon, Long userId) {
        coupon.decreaseQuantity();
        return new UserCoupon(coupon, userId, LocalDateTime.now());
    }

    public void use() {
        if (this.usedAt != null) throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        if (coupon.isExpired()) throw new IllegalStateException("만료된 쿠폰입니다.");
        this.usedAt = LocalDateTime.now();
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }

    public BigDecimal calculateDiscountAmount(BigDecimal orderTotalAmount) {
        validateUsability();
        return coupon.calculateDiscount(orderTotalAmount);
    }


    private void validateUsability() {
        if (isUsed()) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다.");
        }
        if (coupon.isExpired()) {
            throw new IllegalStateException("만료된 쿠폰입니다.");
        }
    }
}
