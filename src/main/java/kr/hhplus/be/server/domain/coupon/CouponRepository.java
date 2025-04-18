package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findById(Long couponId);

    Optional<FlatDiscountCoupon> findFlatCouponById(Long couponId);

    Coupon save(Coupon coupon);
}
