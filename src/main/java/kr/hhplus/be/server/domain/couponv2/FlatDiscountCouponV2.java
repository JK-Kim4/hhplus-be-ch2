package kr.hhplus.be.server.domain.couponv2;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlatDiscountCouponV2 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flat_discount_coupon_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private CouponV2 coupon;

    @Column
    private Integer discountAmount;

    public FlatDiscountCouponV2(CouponV2 coupon, Integer discountAmount) {
        this.coupon = coupon;
        coupon.flatDiscountCoupon = this;
        this.discountAmount = discountAmount;
    }

}
