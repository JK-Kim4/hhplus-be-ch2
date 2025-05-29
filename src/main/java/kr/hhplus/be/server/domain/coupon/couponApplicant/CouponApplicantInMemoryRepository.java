package kr.hhplus.be.server.domain.coupon.couponApplicant;

import java.util.List;
import java.util.Optional;

public interface CouponApplicantInMemoryRepository {

    void registerCouponApplicant(Long couponId, Long userId, long millis);

    boolean containsCouponApplicant(Long couponId, Long userId);

    Optional<CouponApplicant> findByCouponIdAndUserId(Long couponId, Long userId);

    Optional<CouponApplicant> findWinnerByCouponIdAndUserId(Long couponId, Long userId);

    List<CouponApplicant> findByCouponId(Long couponId);

    boolean existIssuableCoupon(Long couponId);

    List<Long> findIssuableCouponIds();

    void deleteIssuableCouponKey(Long couponId);

    void deleteCouponApplicantKey(Long couponId);

    void registerCouponIssuableKey(Long couponId);
}
