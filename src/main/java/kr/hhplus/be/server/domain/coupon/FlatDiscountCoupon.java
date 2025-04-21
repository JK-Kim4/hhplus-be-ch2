package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlatDiscountCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flat_discount_coupon_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Coupon coupon;

    @Column
    private Integer discountAmount;

    public FlatDiscountCoupon(Coupon coupon, Integer discountAmount) {
        this.coupon = coupon;
        coupon.flatDiscountCoupon = this;
        this.discountAmount = discountAmount;
    }

}
