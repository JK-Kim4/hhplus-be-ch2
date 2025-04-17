package kr.hhplus.be.server.domain.couponv2;

import java.util.Optional;

public interface UserCouponV2Repository {
    UserCouponV2 save(UserCouponV2 userCouponV2);

    Optional<UserCouponV2> findByUserIdAndCouponId(Long userId, Long couponId);

    boolean isAlreadyIssuedCoupon(Long userId, Long couponId);

    Optional<UserCouponV2> findById(Long userCouponId);

}
