package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {

    void flush();

    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findById(Long userId);

    Optional<UserCoupon> findByCouponIdAndUserId(Long couponId, Long userId);

    List<UserCoupon> findByUserId(Long userId);
}
