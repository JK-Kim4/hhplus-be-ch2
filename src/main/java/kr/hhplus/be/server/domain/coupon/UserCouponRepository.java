package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface UserCouponRepository {

    void flush();

    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByCouponIdAndUserId(Long couponId, Long userId);
}
