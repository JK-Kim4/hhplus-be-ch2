package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    boolean isAlreadyIssuedCoupon(Long userId, Long couponId);

    Optional<UserCoupon> findById(Long userCouponId);

}
