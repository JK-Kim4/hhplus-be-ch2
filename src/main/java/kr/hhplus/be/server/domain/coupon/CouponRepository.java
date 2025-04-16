package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface CouponRepository {

    void save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);
}
