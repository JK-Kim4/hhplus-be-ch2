package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository {
    Optional<UserCoupon> findByCouponIdAndUserId(Long couponId, Long userId);

    UserCoupon save(UserCoupon userCoupon);
}
