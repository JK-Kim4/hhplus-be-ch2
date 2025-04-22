package kr.hhplus.be.server.domain.coupon.userCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByCouponIdAndUserId(Long userId, Long couponId);

    boolean isAlreadyIssuedCoupon(Long userId, Long couponId);

    Optional<UserCoupon> findById(Long userCouponId);

    List<UserCoupon> findByUserId(Long userId);

}
